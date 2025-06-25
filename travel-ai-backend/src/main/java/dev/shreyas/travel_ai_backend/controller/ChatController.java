package dev.shreyas.travel_ai_backend.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.shreyas.travel_ai_backend.dto.TravelContextDto;
import dev.shreyas.travel_ai_backend.service.LLMService;

@RestController
public class ChatController {
  private final ChatModel chatModel;
  private final LLMService llmService;

  public ChatController(ChatModel chatModel, LLMService llmService) {
    this.chatModel = chatModel;
    this.llmService = llmService;
  }

  @PostMapping("/chat")
  public String chat(@RequestBody TravelContextDto travelContext) throws Exception {
    return llmService.generateTravelPlan(travelContext);
  }
}
