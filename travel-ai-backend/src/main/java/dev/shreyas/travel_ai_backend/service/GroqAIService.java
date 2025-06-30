package dev.shreyas.travel_ai_backend.service;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;


import dev.shreyas.travel_ai_backend.dto.TravelContextDto;

public class GroqAIService implements LLMService {

    private final ChatModel chatModel;

    public GroqAIService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public String generateTravelPlan(TravelContextDto travelRequest) throws Exception {
        String prompt = String.format("Create a travel plan for %d days in %s with a %s style and a budget of %.2f.",
                travelRequest.getTravelDays(),
                travelRequest.getDestination(),
                travelRequest.getTravelStyle(),
                travelRequest.getBudget());

        return ChatClient.create(chatModel)
                .prompt(prompt)
                .call()
                .content();
    }
}
