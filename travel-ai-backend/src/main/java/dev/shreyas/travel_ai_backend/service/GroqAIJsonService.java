package dev.shreyas.travel_ai_backend.service;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import dev.shreyas.travel_ai_backend.dto.TravelContextDto;

@Service
public class GroqAIJsonService implements LLMService {

  private final ChatModel chatModel;

  public GroqAIJsonService(ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  @Override
  public String generateTravelPlan(TravelContextDto travelRequest) throws Exception {
    String systemPrompt = """
            You are a travel planner that always responds in valid JSON format.
            Structure your response as:
            {
                "dailyPlans": [ … ],
                "totalCost": 0.0,
                "recommendations": ["string"]
            }
            Ensure that the "dailyPlans" is an array of objects, each containing:
            {
                "day": 1,
                "activities": ["activity1", "activity2"],
                "cost": 0.0
            }
            """;

    String userPrompt = String.format(
            "Create a travel plan for %d days in %s with a %s style and a budget of %.2f.",
            travelRequest.getTravelDays(),
            travelRequest.getDestination(),
            travelRequest.getTravelStyle(),
            travelRequest.getBudget()
    );

    return ChatClient.create(chatModel)
            .prompt()                      // start building your chat
            .system(systemPrompt)         // ▼ system message (your JSON schema instructions) :contentReference[oaicite:0]{index=0}
            .user(userPrompt)             // ▼ user message (the actual travel-plan request)
            .call()
            .content();
  }
}