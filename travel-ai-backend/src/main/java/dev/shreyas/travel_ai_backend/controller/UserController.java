package dev.shreyas.travel_ai_backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

  @GetMapping("/info")
  public Map<String, Object> userInfo(@AuthenticationPrincipal OAuth2User principal) {
    if (principal == null) {
      throw new IllegalStateException("Authentication required. Please log in.");
    }
    // Return the user's attributes, which typically include name, email, and other profile information
    return principal.getAttributes();
  }
}