package com.GPT.prompt_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint; // For unauthorized entry point
import org.springframework.security.web.csrf.CookieCsrfTokenRepository; // For CSRF if we re-enable

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // If re-enabling CSRF and using JS frontend, CookieCsrfTokenRepository is good.
        // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        // .ignoringRequestMatchers("/api/auth/**", "/ws/**") // Example for ignoring specific paths
        http
                .csrf(AbstractHttpConfigurer::disable // TEMPORARY: Keeping CSRF disabled for now
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll() // Endpoints for signup and login
                        // .requestMatchers("/ws/**").permitAll() // For WebSockets later
                        // .requestMatchers("/", "/index.html", "/css/**", "/js/**").permitAll() // For frontend static files later
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/api/auth/login") // Define the custom login processing URL
                        .usernameParameter("username") // Optional: if your form field isn't 'username'
                        .passwordParameter("password") // Optional: if your form field isn't 'password'
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Login successful\", \"username\":\"" + authentication.getName() + "\"}");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Login failed: " + exception.getMessage() + "\"}");
                        })
                        .permitAll() // Allow access to the loginProcessingUrl
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Logout successful\"}");
                        })
                        .permitAll()
                )
                .exceptionHandling(eh -> eh // Handles cases where authentication is required but not provided
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );

        return http.build();
    }
}