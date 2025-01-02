package com.example.taskManagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskManagement.models.User;
import com.example.taskManagement.repositories.UserRepository;

/**
 * Service class Handling business logic for User operations
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get user by Id
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Get user by username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /** 
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Update user
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Delete user
     */
    public void deteleUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Check if username exists
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if email exists
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
