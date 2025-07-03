package dev.shreyas.travel_ai_backend.dto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelPlan {
  private String destination;
  private Integer travelDays; // in days
  private String travelStyle; // e.g., "adventure", "cultural", "relaxation"
  private List<DailyPlan> dailyPlans;
  private double totalCost;
  private List<String> recommendations;
  // Utility method to print summary
  public void printSummary() {
    System.out.println("=== Travel Plan Summary ===");
    System.out.printf("Total Cost: $%.2f%n", totalCost);
    System.out.println("\nDaily Plans:");

    for (DailyPlan day : dailyPlans) {
      System.out.printf("Day %d (Cost: $%.2f):%n", day.getDay(), day.getCost());
      for (Activity activity : day.getActivities()) {
        System.out.println("  • " + activity);
      }
    }

    System.out.println("\nRecommendations:");
    for (String rec : recommendations) {
      System.out.println("  ★ " + rec);
    }
  }
}