package com.example.taskManagement.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

/**
 * This class handles JWT token generation, validation, and user extraction
 * JWT (JSON Web Token) is used for stateless authentication
 */
@Component
public class JwtTokenProvider {
    // Logger for error and info messages
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

     
    // Secret key from application.properties used to sign the JWT
    @Value("${app.jwtSecret}")
    private String jwtSecret;
     
    // Token expiration time in millisenconds from application.properties
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    /**
     * Cretes a secure signing key from the JWT secret
     * This method converts the string secret into a cryptographically secure key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token for an authenticated user
     * @param auth The authentication object containing user details
     * @return A JWT token string
     */
    public String generateToken(Authentication auth) {
        UserDetails userPrincipal = (UserDetails) auth.getPrincipal();

        // Set token expiration time
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        
        // Build the JWT token
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // User indentifier
                .setIssuedAt(new Date())                 // Token creation time
                .setExpiration(expiryDate)               // Token expiry time
                .signWith(getSigningKey())          // Sign tken with secure key
                .compact();
    }
    
    /**
     * Extracts username from JWT token
     * @param token The JWT token
     * @return Username stored in the token
     */
    public String getUsernameFromJWT(String token) {
        // Parse the token and extract claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();     // Return username from token
    }
    
    /**
     * Validate a JWT token
     * @param authToken The token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String authToken) {
        try {
            // Attempt to parse and validate the token
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build().parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.error("JWT token expired: {}", ex.getMessage());
            throw new ExpiredJwtException(ex.getHeader(), ex.getClaims(), "Token has expired");
        } catch (SecurityException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
            throw new SecurityException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
            throw new MalformedJwtException("Invalid JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token: {}", ex.getMessage());
            throw new UnsupportedJwtException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string empty: {}", ex.getMessage());
            throw new IllegalArgumentException("JWT claims string is empty");
        }
    }

}
