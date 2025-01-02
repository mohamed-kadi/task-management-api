package com.example.taskManagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskManagement.models.User;
import com.example.taskManagement.services.UserService;

/**
 * Controller for handling User-related operations
 * Some endpoints are restricted to ADMIN role only
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get all users (ADMIN only)
     * Uses wildcard response type for flexibility in error handling
     * 
     * @return List of all users or error message
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get user by ID
     * Restrict to ADMIN or the user themselves
     * 
     * @param id user ID to retireve
     * @return User if found, 404 if not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#id).get().getUsername() == authentication.name")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update user details
     * Can only be done by ADMIN or user themselves
     * 
     * @param id User ID to update
     * @return Updated user information
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#id).getUsername() == authentication.name")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (!userService.getUserById(id).isPresent())
            return ResponseEntity.notFound().build();
        user.setId(id);
        return ResponseEntity.ok(userService.updateUser(user));
    }
    
    /**
     * Delete user (ADMIN only)
     * 
     * @param id User ID to delete
     * @return failure/success response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userService.getUserById(id).isPresent())
            return ResponseEntity.notFound().build();
        userService.deteleUser(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Get current user's profile
     * Available to any authenticated user
     * Uses security context to identify user
     * @param userDetails Automatically injected from securoty context
     * @return Current user's profile
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserByUsername(userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    


}
