package com.GPT.prompt_app.repository;

import com.GPT.prompt_app.model.User; // Import your User entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Optional, but good practice

import java.util.Optional;

@Repository // 1. Marks this interface as a Spring Data repository (and a Spring bean)
public interface UserRepository extends JpaRepository<User, Long> { // 2. Extends JpaRepository

    // 3. Custom query method to find a user by their username
    // Spring Data JPA will automatically implement this method based on its name.
    Optional<User> findByUsername(String username);

    // 4. Custom query method to check if a user exists with the given username
    // Spring Data JPA will also automatically implement this.
    Boolean existsByUsername(String username);

    // JpaRepository already provides common methods like:
    // - save(User entity)
    // - findById(Long id)
    // - findAll()
    // - deleteById(Long id)
    // - count()
    // ...and many more. You don't need to declare these.
}