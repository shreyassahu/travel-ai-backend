package dev.shreyas.travel_ai_backend.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.shreyas.travel_ai_backend.dto.TravelContextDto;
import dev.shreyas.travel_ai_backend.dto.TravelPlan;
import dev.shreyas.travel_ai_backend.model.AIData;
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
    TravelPlan travelPlan = JsonStreamingParser.extractTravelPlan(response);
    assert travelPlan != null;
    travelPlan.setDestination(travelContext.getDestination());
    travelPlan.setTravelDays(travelContext.getTravelDays());
    travelPlan.setTravelStyle(travelContext.getTravelStyle());
    travelPlan.printSummary();
    // Save the travel plan to the database
    aiDataRepository.save(AIData.builder()
      .destination(travelPlan.getDestination())
      .travelDays(travelPlan.getTravelDays())
      .travelStyle(travelPlan.getTravelStyle())
                    .dailyPlans(travelPlan.getDailyPlans().stream()
                        .map(Object::toString) // Convert DailyPlan objects to String
                        .toList())
      .build());
    return travelPlan;
  }
}
