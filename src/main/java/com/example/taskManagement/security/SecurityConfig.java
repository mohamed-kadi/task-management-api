package com.example.taskManagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security settings.
 * This class defines security rules, authentication, and authorization settings
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
        
    /**
    * Defines the password encoder to be used for encoding user passwords.
    * Uses BCrypt hashing algorithm for password security.
    * @ return The password encoder instance
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication manager.
     * This is used for authenticating user credentials.
     * 
     * @param authenticationConfiguration
     * @return The authentication manager instance
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the data Access Object (DAO) Authentication Provider
     * This bean connects our user details service with password encoding and sets up the core authentication mechanism
     * @return Configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
           // Create new authentication provider instance
           DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
           // Set the user details service to load user data
           // This tells Spring Security how to find and load user details
           // from our database during authentication
           authProvider.setUserDetailsService(userDetailsService);
           // Set password encoder for secure password comparison
           // during the authentication process
           authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //Disable CSRF as we are using JWT
                .csrf(csrf -> {
                    csrf.disable();
                    // Specifically disable CSRF for H2 console
                    csrf.ignoringRequestMatchers("/h2-console/**", "/api/auth/**");
                })
                .cors(cors -> cors.disable())

                // H2 Console needs frames to work
                .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()))
                // Configure the authentication provider
                // This tells Spring Security:
                // 1.How to load user details (through CustomUserDetailsService)
                // 2. How to verify passwords (using BCryptPasswordEncoder)
                // 3. Where to find user credentials during authentication
                .authenticationProvider(authenticationProvider())
            
                // Session management - stateless as we are using JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // All othr endpoints require authentication
                .anyRequest().authenticated())
                // Add JWT filter before UserNamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                

        return http.build();
    }

}
