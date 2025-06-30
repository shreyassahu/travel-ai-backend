package dev.shreyas.travel_ai_backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

import dev.shreyas.travel_ai_backend.dto.Activity;
import dev.shreyas.travel_ai_backend.dto.DailyPlan;
import dev.shreyas.travel_ai_backend.dto.TravelPlan;

public class JsonStreamingParser {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Extracts JSON from a text file containing markdown code blocks
   *
   * @param text The input text containing JSON in ```json``` blocks
   * @return Parsed TravelPlan object or null if extraction fails
   */
  public static TravelPlan extractTravelPlan(String text) {
    String jsonString = extractJsonString(text);

    if (jsonString != null) {
      try {
        // First parse to JsonNode for flexibility
        JsonNode rootNode = objectMapper.readTree(jsonString);
        return parseTravelPlan(rootNode);
      } catch (Exception e) {
        System.err.println("Error parsing JSON: " + e.getMessage());
        e.printStackTrace();
      }
    }

    return null;
  }

  /**
   * Extracts JSON string from markdown code blocks
   */
  private static String extractJsonString(String text) {
    // Pattern to match ```json ... ```
    Pattern pattern = Pattern.compile("```json\\s*([\\s\\S]*?)\\s*```");
    Matcher matcher = pattern.matcher(text);

    if (matcher.find()) {
      return matcher.group(1).trim();
    }

    // Fallback: try to find raw JSON
    pattern = Pattern.compile("\\{[\\s\\S]*\\}");
    matcher = pattern.matcher(text);

    if (matcher.find()) {
      return matcher.group().trim();
    }

    return null;
  }

  /**
   * Parses JsonNode into TravelPlan object
   */
  private static TravelPlan parseTravelPlan(JsonNode rootNode) {
    TravelPlan plan = new TravelPlan();

    // Parse total cost
    if (rootNode.has("totalCost")) {
      plan.setTotalCost(rootNode.get("totalCost").asDouble());
    }

    // Parse daily plans
    if (rootNode.has("dailyPlans")) {
      JsonNode dailyPlansNode = rootNode.get("dailyPlans");
      List<DailyPlan> dailyPlans = new ArrayList<>();

      for (JsonNode dayNode : dailyPlansNode) {
        DailyPlan dailyPlan = parseDailyPlan(dayNode);
        dailyPlans.add(dailyPlan);
      }

      plan.setDailyPlans(dailyPlans);
    }

    // Parse recommendations
    if (rootNode.has("recommendations")) {
      JsonNode recsNode = rootNode.get("recommendations");
      List<String> recommendations = new ArrayList<>();

      for (JsonNode rec : recsNode) {
        recommendations.add(rec.asText());
      }

      plan.setRecommendations(recommendations);
    }

    return plan;
  }

  /**
   * Parses a single day plan
   */
  private static DailyPlan parseDailyPlan(JsonNode dayNode) {
    DailyPlan dailyPlan = new DailyPlan();

    if (dayNode.has("day")) {
      dailyPlan.setDay(dayNode.get("day").asInt());
    }

    if (dayNode.has("cost")) {
      dailyPlan.setCost(dayNode.get("cost").asDouble());
    }

    if (dayNode.has("activities")) {
      JsonNode activitiesNode = dayNode.get("activities");
      List<Activity> activities = new ArrayList<>();

      for (JsonNode activityNode : activitiesNode) {
        Activity activity = new Activity(activityNode.asText());
        activities.add(activity);
      }

      dailyPlan.setActivities(activities);
    }

    return dailyPlan;
  }
}
