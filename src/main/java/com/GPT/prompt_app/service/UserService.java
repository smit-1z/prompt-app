package com.GPT.prompt_app.service;

import com.GPT.prompt_app.model.User;
import com.GPT.prompt_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // 1. Import PasswordEncoder
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 2. Add PasswordEncoder dependency

    @Autowired
    // 3. Inject PasswordEncoder via the constructor
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Assign the injected bean
    }

    public User registerUser(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Error: Username '" + username + "' is already taken!");
        }

        User newUser = new User();
        newUser.setUsername(username);

        // 4. Encode the raw password using the PasswordEncoder
        String encodedPassword = passwordEncoder.encode(rawPassword);
        newUser.setPassword(encodedPassword); // Set the HASHED password

        // REMOVE the old line: newUser.setPassword(rawPassword); // This should be gone

        return userRepository.save(newUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}