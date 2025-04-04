package com.example.flexiMed.controller.auth;

import com.example.flexiMed.security.JwtUtil;
import com.example.flexiMed.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling OAuth2 authentication-related requests.
 * This controller provides an endpoint to generate a JWT token for users authenticated via OAuth2.
 */
@RestController
@RequestMapping("/api/auth/oauth2")
public class OAuth2AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * Constructor to inject the required services.
     *
     * @param jwtUtil Utility class for generating JWT tokens.
     * @param userService Service class for managing user-related operations.
     */
    public OAuth2AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Endpoint to retrieve a JWT token for the authenticated user via OAuth2.
     * This method ensures that the user is authenticated via OAuth2 and then generates
     * a JWT token containing the user's email.
     *
     * @param authentication The authentication object, which contains the user's details.
     * @return A response containing the generated JWT token.
     * @throws IllegalStateException If the user is not authenticated via OAuth2.
     */
    @GetMapping("/token")
    private TokenResponse getOAuth2Token(Authentication authentication) {
        // Ensure the user is authenticated via OAuth2
        if (!(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
            throw new IllegalStateException("User is not authenticated via OAuth2");
        }

        // Retrieve the email attribute from the OAuth2User
        Authentication email = oAuth2User.getAttribute("email");

        // Ensure email is not null
        assert email != null;

        // Generate the JWT token using the email
        String token = jwtUtil.generateToken(email);

        // Return the generated token as a response
        return new TokenResponse(token);
    }

    /**
     * A record class representing the response containing the generated JWT token.
     * The token is provided to the client as part of the response to the token request.
     */
    record TokenResponse(String token) {
    }
}
