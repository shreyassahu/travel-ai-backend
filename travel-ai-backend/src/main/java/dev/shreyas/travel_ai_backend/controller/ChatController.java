package dev.shreyas.travel_ai_backend.controller;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.shreyas.travel_ai_backend.dto.TravelContextDto;
import dev.shreyas.travel_ai_backend.dto.TravelPlan;
import dev.shreyas.travel_ai_backend.service.LLMService;
import dev.shreyas.travel_ai_backend.util.JsonStreamingParser;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

  private final LLMService llmService;

  public ChatController(LLMService llmService) {
    this.llmService = llmService;
  }

  @PostMapping("/chat")
  public TravelPlan chat(@RequestBody TravelContextDto travelContext) throws Exception {
    String response = llmService.generateTravelPlan(travelContext);
    TravelPlan travelPlan = JsonStreamingParser.extractTravelPlan(response);
    if(travelPlan != null) {
      travelPlan.printSummary();
    }
    return travelPlan;
  }
}
