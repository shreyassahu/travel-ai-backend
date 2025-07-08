package dev.shreyas.travel_ai_backend.service;

import java.util.List;

import dev.shreyas.travel_ai_backend.model.AIData;
import dev.shreyas.travel_ai_backend.model.DailyPlan;

public interface DBService {

    AIData saveItinerary(AIData aiData);


    List<AIData> getLatestItineraries();

    void deleteItinerary(String itineraryId);

    AIData getItineraryById(String id);
}
