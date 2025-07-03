package dev.shreyas.travel_ai_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import dev.shreyas.travel_ai_backend.model.AIData;

@Repository
public interface AIDataRepository extends MongoRepository<AIData, String> {
    // Basic save operation is inherited from MongoRepository: save(AIData entity)

    // Find by destination
    List<AIData> findByDestination(String destination);

    // Find by travel style
    List<AIData> findByTravelStyle(String travelStyle);

    // Find by destination and duration
    List<AIData> findByDestinationAndTravelDays(String destination, Integer duration);

    // Find by destination and travel style
    List<AIData> findByDestinationAndTravelStyle(String destination, String travelStyle);
}
