package dev.shreyas.travel_ai_backend.service;

import dev.shreyas.travel_ai_backend.dto.TravelContextDto;

public interface LLMService {
    String generateTravelPlan(TravelContextDto travelRequest) throws Exception;
}
