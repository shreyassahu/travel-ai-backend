package dev.shreyas.travel_ai_backend.dto;
import java.util.List;
import java.util.ArrayList;

public class TravelPlan {
  private List<DailyPlan> dailyPlans;
  private double totalCost;
  private List<String> recommendations;

  // Constructor
  public TravelPlan() {
    this.dailyPlans = new ArrayList<>();
    this.recommendations = new ArrayList<>();
  }

  // Getters and Setters
  public List<DailyPlan> getDailyPlans() {
    return dailyPlans;
  }

  public void setDailyPlans(List<DailyPlan> dailyPlans) {
    this.dailyPlans = dailyPlans;
  }

  public double getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(double totalCost) {
    this.totalCost = totalCost;
  }

  public List<String> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<String> recommendations) {
    this.recommendations = recommendations;
  }

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