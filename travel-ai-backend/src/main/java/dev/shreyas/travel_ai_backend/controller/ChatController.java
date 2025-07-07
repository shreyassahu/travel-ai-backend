package dev.shreyas.travel_ai_backend.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.shreyas.travel_ai_backend.dto.TravelContextDto;
import dev.shreyas.travel_ai_backend.dto.TravelPlan;
import dev.shreyas.travel_ai_backend.model.AIData;
import dev.shreyas.travel_ai_backend.model.DailyPlan;
import dev.shreyas.travel_ai_backend.repository.AIDataRepository;
import dev.shreyas.travel_ai_backend.service.LLMService;
import dev.shreyas.travel_ai_backend.util.JsonStreamingParser;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

  private final LLMService llmService;
  private final AIDataRepository aiDataRepository;

  public ChatController(LLMService llmService, AIDataRepository aiDataRepository) {
    this.llmService = llmService;
    this.aiDataRepository = aiDataRepository;
  }

  @PostMapping("/chat")
  public TravelPlan chat(@RequestBody TravelContextDto travelContext) throws Exception {
    String response = llmService.generateTravelPlan(travelContext);

    // Log the raw response
    System.out.println("Raw AI Response:");
    System.out.println(response);
    System.out.println("End of Raw AI Response");

    try {
      TravelPlan travelPlan = JsonStreamingParser.extractTravelPlan(response);
      if (travelPlan != null) {
        travelPlan.setDestination(travelContext.getDestination());
        travelPlan.setTravelDays(travelContext.getTravelDays());
        travelPlan.setTravelStyle(travelContext.getTravelStyle());
        travelPlan.printSummary();

        aiDataRepository.save(AIData.builder()
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
                .build());
        return travelPlan;
      } else {
        throw new IllegalStateException("Failed to parse travel plan from AI response");
      }
    } catch (Exception e) {
      System.err.println("Error processing AI response: " + e.getMessage());
      e.printStackTrace();
      throw new IllegalStateException("Failed to process AI response: " + e.getMessage(), e);
    }
  }
}