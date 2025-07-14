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
            .csrf(csrf -> csrf.disable())  // Disable CSRF for API endpoints
            .cors(Customizer.withDefaults())  // Enable CORS with default configuration
            .authorizeHttpRequests(auth -> auth// Require auth for dashboard
                .anyRequest().permitAll()
            )
                .oauth2Login(Customizer.withDefaults());


        return http.build();
    }
}
