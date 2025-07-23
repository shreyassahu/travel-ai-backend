package dev.shreyas.travel_ai_backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIDataDto {
  private String id;
  private String userId;
  private String destination;
  private Integer travelDays;
  private String travelStyle;
  private List<DailyPlanDto> dailyPlans;
  private Double totalCost;
  private List<String> recommendations;
}
