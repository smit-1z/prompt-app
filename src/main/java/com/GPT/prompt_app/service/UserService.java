package com.GPT.prompt_app.service;
// Make sure this matches your package structure

import com.GPT.prompt_app.model.User;
import com.GPT.prompt_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// We will import PasswordEncoder later
// import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service // 1. Marks this class as a Spring service component
public class UserService {

    private final UserRepository userRepository; // 2. Dependency for user data access
    // private PasswordEncoder passwordEncoder; // 3. We'll inject this later for password hashing

    @Autowired // 4. Constructor-based dependency injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
// When PasswordEncoder is added, inject it here too:
// this.passwordEncoder = passwordEncoder;
    }

    // Method for user registration
    public User registerUser(String username, String rawPassword) {
        // 5. Check if username already exists
        if (userRepository.existsByUsername(username)) {
            // It's generally better to throw a custom exception here
            throw new RuntimeException("Error: Username '" + username + "' is already taken!");
        }

        User newUser = new User();
        newUser.setUsername(username);

        // 6. IMPORTANT: Password Hashing will be done here later
        // For now, storing raw password (NOT FOR PRODUCTION)
        // String encodedPassword = passwordEncoder.encode(rawPassword);
        // newUser.setPassword(encodedPassword);
        newUser.setPassword(rawPassword); // TEMPORARY - WILL BE REPLACED

        return userRepository.save(newUser); // 7. Save the new user to the database
    }

    // Method to find a user by their username (useful for login)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // You can add other user-related business logic methods here, e.g.:
    // - updateUserProfile(Long userId, UserProfileDetails details)
    // - changePassword(Long userId, String oldPassword, String newPassword)
    // - etc.
}