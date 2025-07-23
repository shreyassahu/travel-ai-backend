package dev.shreyas.travel_ai_backend.model;

import java.util.List;

import dev.shreyas.travel_ai_backend.dto.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class   DailyPlan {
  private int day;
  private List<Activity> activities;
  private Double cost;
}