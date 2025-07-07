package dev.shreyas.travel_ai_backend.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyPlan {
    @JsonProperty("day")
    private int day;

    @JsonProperty("activities")
    private List<Activity> activities;

    @JsonProperty("cost")
    private double cost;
}
