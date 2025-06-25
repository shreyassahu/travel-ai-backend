package dev.shreyas.travel_ai_backend.dto;

import lombok.Data;

@Data
public class TravelContextDto {
  private int travelDays;
  private String destination;
  private String travelStyle;
  private Double budget;
}
