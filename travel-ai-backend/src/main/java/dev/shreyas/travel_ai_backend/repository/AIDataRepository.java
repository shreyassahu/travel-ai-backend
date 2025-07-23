package dev.shreyas.travel_ai_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import dev.shreyas.travel_ai_backend.model.AIData;

@Repository
public interface AIDataRepository extends MongoRepository<AIData, String> {
    // Basic save operation is inherited from MongoRepository: save(AIData entity)

    // Find by destination
    List<AIData> findItinerariesByUserId(String userId);

    // Find latest 10 itineraries
    List<AIData> findTop10ByOrderByCreatedAtDesc();

    // Find itinerary by ID
    Optional<AIData> findById(String id);
}
