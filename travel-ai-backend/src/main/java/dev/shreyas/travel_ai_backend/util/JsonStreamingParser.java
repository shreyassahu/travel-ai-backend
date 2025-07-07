package dev.shreyas.travel_ai_backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

import dev.shreyas.travel_ai_backend.dto.Activity;
import dev.shreyas.travel_ai_backend.dto.DailyPlan;
import dev.shreyas.travel_ai_backend.dto.TravelPlan;

public class JsonStreamingParser {

  private static final ObjectMapper objectMapper = new ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

  /**
   * Extracts JSON from a text file containing markdown code blocks
   *
   * @param text The input text containing JSON in ```json``` blocks
   * @return Parsed TravelPlan object or null if extraction fails
   */
  public static TravelPlan extractTravelPlan(String text) {
    if (text == null || text.trim().isEmpty()) {
      throw new IllegalArgumentException("Input text cannot be null or empty");
    }

    String jsonString = extractJsonString(text);
    if (jsonString == null) {
      throw new IllegalArgumentException("Could not find valid JSON in the input");
    }

    try {
      // Try direct deserialization first
      try {
        return objectMapper.readValue(jsonString, TravelPlan.class);
      } catch (JsonProcessingException e) {
        // If direct deserialization fails, try parsing through JsonNode
        JsonNode rootNode = objectMapper.readTree(jsonString);
        return parseTravelPlan(rootNode);
      }
    } catch (Exception e) {
      System.err.println("Error parsing JSON: " + e.getMessage());
      e.printStackTrace();
      throw new IllegalArgumentException("Failed to parse JSON: " + e.getMessage(), e);
    }
  }

  /**
   * Extracts JSON string from markdown code blocks
   */
  private static String extractJsonString(String text) {
    if (text == null || text.trim().isEmpty()) {
      return null;
    }

    text = text.trim();

    // Case 1: Direct JSON
    if (text.startsWith("{") && text.endsWith("}")) {
      return text;
    }

    // Case 2: JSON in markdown code blocks
    Pattern pattern = Pattern.compile("```(?:json)?\\s*\\{([\\s\\S]*?)\\}\\s*```");
    Matcher matcher = pattern.matcher(text);
    if (matcher.find()) {
      return "{" + matcher.group(1).trim() + "}";
    }

    // Case 3: Find any JSON-like structure
    pattern = Pattern.compile("\\{([\\s\\S]*?)\\}");
    matcher = pattern.matcher(text);
    if (matcher.find()) {
      return "{" + matcher.group(1).trim() + "}";
    }

    // Case 4: Clean up the text and try to find JSON
    String cleaned = text.replaceAll("(?s).*?\\{", "{")  // Remove everything before first {
                        .replaceAll("\\}.*", "}")         // Remove everything after last }
                        .trim();

    if (cleaned.startsWith("{") && cleaned.endsWith("}")) {
      return cleaned;
    }

    return null;
  }

  /**
   * Parses JsonNode into TravelPlan object
   */
  private static TravelPlan parseTravelPlan(JsonNode rootNode) {
    TravelPlan.TravelPlanBuilder builder = TravelPlan.builder();

    // Parse total cost
    if (rootNode.has("totalCost")) {
      builder.totalCost(rootNode.get("totalCost").asDouble());
    }

    // Parse daily plans
    if (rootNode.has("dailyPlans")) {
      List<DailyPlan> dailyPlans = new ArrayList<>();
      JsonNode dailyPlansNode = rootNode.get("dailyPlans");

      if (dailyPlansNode.isArray()) {
        for (JsonNode dayNode : dailyPlansNode) {
          DailyPlan.DailyPlanBuilder dayBuilder = DailyPlan.builder();

          if (dayNode.has("day")) {
            dayBuilder.day(dayNode.get("day").asInt());
          }
          if (dayNode.has("cost")) {
            dayBuilder.cost(dayNode.get("cost").asDouble());
          }

          // Parse activities
          if (dayNode.has("activities")) {
            List<Activity> activities = new ArrayList<>();
            JsonNode activitiesNode = dayNode.get("activities");

            if (activitiesNode.isArray()) {
              for (JsonNode actNode : activitiesNode) {
                try {
                  Activity activity = objectMapper.treeToValue(actNode, Activity.class);
                  activities.add(activity);
                } catch (JsonProcessingException e) {
                  System.err.println("Error parsing activity: " + e.getMessage());
                }
              }
            }
            dayBuilder.activities(activities);
          }

          dailyPlans.add(dayBuilder.build());
        }
      }
      builder.dailyPlans(dailyPlans);
    }

    return builder.build();
  }
}
