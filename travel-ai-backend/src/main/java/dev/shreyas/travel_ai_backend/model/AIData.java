package dev.shreyas.travel_ai_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import dev.shreyas.travel_ai_backend.dto.DailyPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("AIData")
public class AIData {
  @Id
  private String id;
  private String destination;
  private Integer travelDays;
  private String travelStyle;
  private List<String> dailyPlans;  // Changed back to String

}