package com.example.flexiMed.security;

import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;

/**
 * Custom success handler for OAuth2 login.
 * This handler is triggered after a successful OAuth2 authentication (e.g., Google login).
 * It generates a JWT token for the authenticated user and redirects to the front-end application
 * with the JWT token as a query parameter.
 */
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * Constructor for dependency injection.
     *
     * @param jwtUtil The JwtUtil service for generating JWT tokens.
     * @param userService The UserService for accessing user information.
     */
    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * This method is invoked after a successful OAuth2 authentication.
     * It generates a JWT token for the authenticated user and redirects to the front-end application.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param authentication The authentication object, containing the OAuth2 user.
     * @throws IOException If there is an I/O error during redirection.
     * @throws ServletException If the redirection operation fails.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Get the authenticated OAuth2 user (in this case, Google user)
        OAuth2User googleUser = (OAuth2User) authentication.getPrincipal();

        // Extract the email attribute from the OAuth2 user (e.g., from Google login)
        String email = googleUser.getAttribute("email");

        // Fetch the UserEntity from the database using the email
        UserEntity user = userService.loadUserByUsername(email);

        // Create an authentication object from the UserEntity
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, // principal (the authenticated UserEntity object)
                null, // credentials (not needed for JWT generation)
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())) // Set roles for the user
        );

        // Generate a JWT token based on the user's authentication details
        String jwtToken = jwtUtil.generateToken(auth);

        // Build the redirect URL, adding the JWT token as a query parameter
        String redirectUrl = UriComponentsBuilder.fromUriString("https://flexi-med-front-itcp.vercel.app/")
                .queryParam("token", jwtToken) // Add the JWT token to the redirect URL
                .build().toUriString();

        // Perform the redirection to the front-end application with the JWT token
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
