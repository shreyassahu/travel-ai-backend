package dev.shreyas.travel_ai_backend.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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
  private String userId;
  private String destination;
  private Integer travelDays;
  private String travelStyle;
  private List<DailyPlan> dailyPlans;
  private Double totalCost;
  private List<String> recommendations;// Store actual DailyPlan objects

  @Builder.Default
  private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}