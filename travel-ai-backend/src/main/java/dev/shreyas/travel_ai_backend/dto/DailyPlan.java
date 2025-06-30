package dev.shreyas.travel_ai_backend.dto;
import java.util.List;
import java.util.ArrayList;

public class DailyPlan {
  private int day;
  private List<Activity> activities;
  private double cost;

  // Constructor
  public DailyPlan() {
    this.activities = new ArrayList<>();
  }

  // Getters and Setters
  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public List<Activity> getActivities() {
    return activities;
  }

  public void setActivities(List<Activity> activities) {
    this.activities = activities;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }
}
