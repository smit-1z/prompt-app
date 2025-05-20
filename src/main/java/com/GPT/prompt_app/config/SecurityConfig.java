package com.GPT.prompt_app.config; // Your package structure

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults; // For default configurations

@Configuration // 1. Marks this class as a source of bean definitions
@EnableWebSecurity // 2. Enables Spring Security's web security support
public class SecurityConfig {

    // 3. Define a Bean for PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Using BCrypt for strong password hashing
    }

    // 4. Define a Bean for SecurityFilterChain to configure HTTP security rules
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // TEMPORARY: Disable CSRF for now, especially for simpler API testing. We'll revisit this.
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll() // Allow access to signup/login endpoints
                        // .requestMatchers("/ws/**").permitAll() // We'll add WebSocket permissions later
                        // .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/favicon.ico").permitAll() // Static resources
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .formLogin(withDefaults()); // Enable basic form login with default settings
        // .httpBasic(withDefaults()); // Or enable HTTP Basic authentication

        return http.build();
    }
}