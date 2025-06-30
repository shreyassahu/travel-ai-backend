package dev.shreyas.travel_ai_backend.dto;

public class Activity {
  private String timeOfDay;
  private String description;

  // Constructor
  public Activity(String activityString) {
    parseActivity(activityString);
  }

  // Parse "Morning: Explore the Freedom Trail" format
  private void parseActivity(String activityString) {
    if (activityString.contains(":")) {
      String[] parts = activityString.split(":", 2);
      this.timeOfDay = parts[0].trim();
      this.description = parts[1].trim();
    } else {
      this.timeOfDay = "";
      this.description = activityString.trim();
    }
  }

  // Getters
  public String getTimeOfDay() {
    return timeOfDay;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    if (!timeOfDay.isEmpty()) {
      return timeOfDay + ": " + description;
    }
    return description;
  }
}