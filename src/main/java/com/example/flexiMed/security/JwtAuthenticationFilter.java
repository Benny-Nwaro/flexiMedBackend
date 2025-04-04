package com.example.flexiMed.security;

import com.example.flexiMed.service.UserService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class is a filter that intercepts incoming HTTP requests and validates JWT tokens.
 * It checks the Authorization header for a valid JWT token, extracts the username,
 * and validates the token before setting the user authentication in the Spring Security context.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Logger for logging events within the filter
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;  // Utility for handling JWT token operations
    private final UserService userService;  // Service for loading user details from the database

    /**
     * Constructor for JwtAuthenticationFilter.
     * Initializes the filter with the JwtUtil and UserService dependencies.
     *
     * @param jwtUtil      The utility class for generating and validating JWT tokens.
     * @param userService  The service for retrieving user details by username.
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Determines whether the filter should be applied to the current request.
     * The filter is skipped for paths related to OAuth2, login, swagger UI, and public API documentation.
     *
     * @param request The HTTP request.
     * @return true if the request path should not be filtered, false otherwise.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Skip filtering for OAuth2 authentication flow and other public endpoints
        boolean shouldSkip = path.startsWith("/auth/") || path.startsWith("/oauth2/")
                || path.startsWith("/login") || path.startsWith("/ws") || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs") || path.equals("/swagger-ui.html");

        if (shouldSkip) {
            logger.info("Skipping JWT filter for path: {}", path);
        }

        return shouldSkip;
    }

    /**
     * This method is executed for every request that passes the `shouldNotFilter` check.
     * It extracts the JWT token from the Authorization header, validates it, and sets the authentication
     * in the security context if the token is valid.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @param chain    The filter chain to continue the request processing.
     * @throws ServletException If a servlet-related error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        // Retrieve the Authorization header from the request
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Check if the Authorization header exists and starts with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("No JWT token found or incorrect format for request: {}", request.getRequestURI());
            chain.doFilter(request, response);  // Continue the filter chain if no valid token is found
            return;
        }

        // Extract the token from the "Bearer " prefix
        String token = authorizationHeader.substring(7);
        String username;

        try {
            // Extract the username (subject) from the JWT token
            username = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            // Log error if there's an issue extracting the username from the token
            logger.error("Error extracting username from token: {}", e.getMessage());
            chain.doFilter(request, response);  // Continue the filter chain
            return;
        }

        // Check if the username exists and the user is not already authenticated in the security context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user details from the database
            UserDetails userDetails = userService.loadUserByUsername(username);

            // Validate the JWT token
            if (jwtUtil.validateToken(token, userDetails)) {
                // If the token is valid, create an Authentication object and set it in the security context
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));  // Set details (IP, session ID, etc.)
                SecurityContextHolder.getContext().setAuthentication(authentication);  // Set the authentication in the context
                logger.info("User {} authenticated successfully via JWT", username);
            } else {
                // Log warning if the token is invalid
                logger.warn("Invalid token for user: {}", username);
            }
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }
}
