package dev.shreyas.travel_ai_backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            // 1. authorize requests
            .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/api/**")       // ← skip CSRF only for /api/**
            )
            .authorizeHttpRequests(auth -> auth
                    // allow static resources and the OAuth2 login endpoints
                    .requestMatchers(
                            "/api/**"
                    ).permitAll()
                    // everything else requires authentication
                    .anyRequest().authenticated()
            )
            // 2. enable OAuth2 Login with default settings
            .oauth2Login(Customizer.withDefaults())
            // 3. optionally enable logout
            .logout(logout -> logout
                    .logoutSuccessUrl("/api/chat")
                    .permitAll()
            );

    return http.build();
  }
}
