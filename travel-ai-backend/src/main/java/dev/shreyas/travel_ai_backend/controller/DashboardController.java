package dev.shreyas.travel_ai_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import dev.shreyas.travel_ai_backend.model.AIData;
import dev.shreyas.travel_ai_backend.service.DBService;

@RestController
public class DashboardController {
    private final DBService dbService;

    public DashboardController(DBService dbService) {
        this.dbService = dbService;
    }

  @GetMapping("/dashboard")
  public String dashboard(@AuthenticationPrincipal OAuth2User principal, Model model) {
    // Extract user details from OAuth2User
    String username = principal.getAttribute("name");
    String email = principal.getAttribute("email");

    // Add user details to the model
    model.addAttribute("username", username);
    model.addAttribute("email", email);

    // Return the dashboard view
    return "dashboard";
  }

  @GetMapping("/itinerary/{id}")
  public ResponseEntity<AIData> getItineraryById(@PathVariable String id) {
      try {
          AIData itinerary = dbService.getItineraryById(id);
          return ResponseEntity.ok(itinerary);
      } catch (RuntimeException e) {
          return ResponseEntity.notFound().build();
      }
  }
}