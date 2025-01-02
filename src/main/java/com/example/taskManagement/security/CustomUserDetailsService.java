package com.example.taskManagement.security;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.taskManagement.models.User;
import com.example.taskManagement.repositories.UserRepository;
import jakarta.transaction.Transactional;

/**
 * Custom implementation of Spring Security's UserDetailsService
 * This class bridges our User entity with Spring Security's user representation
 * It loads user-specific data and convert it to Spring Security's UserDetails format
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * Repository to access user date from database
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Loads a user by username from the database and converts it to UserDetails
     * This method is used by Spring Security during authentication
     * 
     * @param username The username to look up
     * @return UserDetails object containing user information
     * @throws UserNameNotFoundException if user not found
     */
    @Override
    @Transactional  // Ensures database transaction handling
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user in database using repository
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> 
                new UsernameNotFoundException("User not found with username: " + username));

        // Convert our custom User object to Spring Security's UserDetails
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername()) // Set username
            .password(user.getPassword())    // Set password (ahould be encoded)
            .authorities(Collections.singletonList( // Set role as authority
                        new SimpleGrantedAuthority(user.getRole().name())))
            .accountExpired(false) // Account is not expired
            .accountLocked(false)   // Account is not locked
            .credentialsExpired(false) // Credentials are not expired
            .disabled(false) // Account is enabled
            .build();
    }


}
