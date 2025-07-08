package dev.shreyas.travel_ai_backend.service;

import org.springframework.stereotype.Service;

import java.util.List;

import dev.shreyas.travel_ai_backend.model.AIData;
import dev.shreyas.travel_ai_backend.repository.AIDataRepository;

@Service
public class DBServiceImpl implements DBService {

    private final AIDataRepository aiDataRepository;

    public DBServiceImpl(AIDataRepository aiDataRepository) {
        this.aiDataRepository = aiDataRepository;
    }

    @Override
    public AIData saveItinerary(AIData aiData) {
        // Implementation for saving AIData to the database
        if (aiData != null) {
            return aiDataRepository.save(aiData);
        } else {
            throw new IllegalArgumentException("AIData cannot be null");
        }
    }

    @Override
    public List<AIData> getLatestItineraries() {
        // Implementation for retrieving the latest 10 itineraries from the database
        return aiDataRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public void deleteItinerary(String itineraryId) {
        // Implementation for deleting daily plans from the database
        if (itineraryId != null && !itineraryId.isEmpty()) {
            aiDataRepository.deleteById(itineraryId);
        } else {
            throw new IllegalArgumentException("Itinerary ID cannot be null or empty");
        }
    }

    @Override
    public AIData getItineraryById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Itinerary ID cannot be null or empty");
        }
        return aiDataRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Itinerary not found with id: " + id));
    }
}
