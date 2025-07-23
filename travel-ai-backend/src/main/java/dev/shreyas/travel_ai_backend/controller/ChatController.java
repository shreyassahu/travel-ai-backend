package dev.shreyas.travel_ai_backend.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import dev.shreyas.travel_ai_backend.dto.TravelContextDto;
import dev.shreyas.travel_ai_backend.dto.TravelPlan;
import dev.shreyas.travel_ai_backend.model.AIData;
import dev.shreyas.travel_ai_backend.model.DailyPlan;
import dev.shreyas.travel_ai_backend.service.DBService;
import dev.shreyas.travel_ai_backend.service.LLMService;
import dev.shreyas.travel_ai_backend.util.JsonStreamingParser;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ChatController {

  private final LLMService llmService;
  private final DBService dbService;

  public ChatController(LLMService llmService, DBService dbService) {
    this.llmService = llmService;
    this.dbService = dbService;
  }

  @PostMapping("/chat")
  public AIData chat(@RequestBody TravelContextDto travelContext, @AuthenticationPrincipal OAuth2User user) throws Exception {

    if (user == null) {
      throw new IllegalStateException("Authentication required. Please log in.");
    }

    String userId = user.getAttribute("sub");  // Get the unique user ID from OAuth provider
    if (userId == null) {
      userId = user.getName();  // Fallback to name if sub not available
    }

    String response = llmService.generateTravelPlan(travelContext);

    try {
      TravelPlan travelPlan = JsonStreamingParser.extractTravelPlan(response);
      if (travelPlan != null) {
        travelPlan.setDestination(travelContext.getDestination());
        travelPlan.setTravelDays(travelContext.getTravelDays());
        travelPlan.setTravelStyle(travelContext.getTravelStyle());
        AIData aiData = dbService.saveItinerary(AIData.builder()
                        .userId(userId)  // Use the actual user ID from OAuth
                .destination(travelPlan.getDestination())
                .travelDays(travelPlan.getTravelDays())
                .travelStyle(travelPlan.getTravelStyle())
                .dailyPlans(travelPlan.getDailyPlans().stream()
                        .map(dtoPlan -> {
                          return DailyPlan.builder()
                                  .day(dtoPlan.getDay())
                                  .activities(dtoPlan.getActivities())
                                  .cost(dtoPlan.getCost())
                                  .build();
                        })
                        .toList())
                        .recommendations(travelPlan.getRecommendations())
                .totalCost(travelPlan.getTotalCost())
                .build());
        return aiData;
      } else {
        throw new IllegalStateException("Failed to parse travel plan from AI response");
      }
    } catch (Exception e) {
      System.err.println("Error processing AI response: " + e.getMessage());
      e.printStackTrace();
      throw new IllegalStateException("Failed to process AI response: " + e.getMessage(), e);
    }
  }

  @GetMapping("/itineraries")
  public List<AIData> getLatestItineraries(@AuthenticationPrincipal OAuth2User user) {
    if (user == null) {
      throw new IllegalStateException("Authentication required. Please log in.");
    }

    String userId = user.getAttribute("sub");
    if (userId == null) {
      userId = user.getName();
    }
    return dbService.getItinerariesByUser(userId);
  }

  @GetMapping("/itinerary/{id}")
  public ResponseEntity<AIData> getItineraryById(@PathVariable String id, @AuthenticationPrincipal OAuth2User user) {
    if (user == null) {
      return ResponseEntity.status(401).build(); // Unauthorized
    }

    try {
      AIData itinerary = dbService.getItineraryById(id);
      return ResponseEntity.ok(itinerary);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}