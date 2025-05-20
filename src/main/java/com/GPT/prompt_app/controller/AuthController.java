package com.GPT.prompt_app.controller; // Your package structure

import com.GPT.prompt_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // To get the logged-in user's details
import java.util.Map;

@RestController // 1. Combines @Controller and @ResponseBody
@RequestMapping("/api/auth") // 2. Base path for all endpoints in this controller
public class AuthController {

    private final UserService userService;

    @Autowired // 3. Inject UserService
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 4. DTO (Data Transfer Object) for signup/login request body
    // Using a static inner class for simplicity here. You could make it a separate public class.
    static class AuthRequest {
        public String username;
        public String password;
        // Ensure getters if you make fields private, or ensure your JSON library can access public fields
    }

    // 5. Endpoint for User Registration
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest signUpRequest) {
        // Basic validation
        if (signUpRequest.username == null || signUpRequest.username.trim().isEmpty() ||
                signUpRequest.password == null || signUpRequest.password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username and password cannot be empty."));
        }

        try {
            userService.registerUser(signUpRequest.username, signUpRequest.password);
            return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
        } catch (RuntimeException e) {
            // Catching the exception from UserService if username is already taken
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An unexpected error occurred."));
        }
    }

    // Note: The actual login processing for POST /api/auth/login is handled by Spring Security's
    // formLogin().successHandler and .failureHandler as configured in SecurityConfig.
    // This controller doesn't need a POST /login method if using that setup.

    // 6. Endpoint to get current authenticated user's information
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            // This case should ideally be handled by Spring Security sending a 401
            // if .anyRequest().authenticated() is hit without auth.
            // But as a fallback or for explicit check:
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No user logged in."));
        }
        // Principal.getName() returns the username by default
        // You could fetch more user details from userService if needed
        return ResponseEntity.ok(Map.of("username", principal.getName()));
    }
}