package com.example.taskManagement.security;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.springframework.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that intercepts every request to validate JWT tokens
 * Extends OncePerRequestFilter to guarantee a single execution per request
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    // Change from field injection to onstructor injection
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService){
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }


    // @Override
    // protected void doFilterInternal(HttpServletRequest request,
    //                                HttpServletResponse response,
    //                                FilterChain filterChain)
    //         throws ServletException, IOException {
    //     try {
    //         // Extract JWT token from request
    //         String jwt = getJwtFromRequest(request);
    //         // Check if token exists and is valid
    //         if (StringUtils.hasLength(jwt) && tokenProvider.validateToken(jwt)) {
    //             // Extract username from token
    //             String username = tokenProvider.getUsernameFromJWT(jwt);

    //             // Load user details from database
    //             UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    //             // Create authentication token
    //             UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());

    //             // Add request details to auth token
    //             auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    //             // Set auth in Security context
    //             SecurityContextHolder.getContext().setAuthentication(auth);
    //         }
    //     } catch (Exception ex) {
    //         logger.error("Could not set user authentication in security context", ex);
    //     }

    //     // Continue with the filter chain
    //     filterChain.doFilter(request, response);
    // }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                              HttpServletResponse response, 
                              FilterChain filterChain) 
        throws ServletException, IOException {
    try {
        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasLength(jwt)) {
            try {
                if (tokenProvider.validateToken(jwt)) {
                    String username = tokenProvider.getUsernameFromJWT(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                logger.error("Expired JWT token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"Token has expired\"}");
                response.getWriter().flush();
                return;
            } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
                logger.error("Invalid JWT token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"Invalid token\"}");
                response.getWriter().flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    } catch (Exception ex) {
        logger.error("Could not set user authentication in security context", ex);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"Authentication failed\"}");
        response.getWriter().flush();
        return;
    }
}
    /**
     * Extracts JWT token from the request header
     * Expects format: "Bearer <token>"
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

