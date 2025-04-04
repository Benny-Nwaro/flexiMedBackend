package com.example.flexiMed.controller.auth;

import com.example.flexiMed.dto.LoginRequestDTO;
import com.example.flexiMed.security.JwtUtil;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling user authentication and registration requests.
 * It supports login and registration, where login generates a JWT token for authenticated users.
 * During registration, the user's details are saved, and the user is automatically logged in.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * Constructor to inject the required services.
     *
     * @param authenticationManager The authentication manager used for authenticating the user.
     * @param jwtUtil The utility class for generating JWT tokens.
     * @param userService The service for managing user-related operations.
     */
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Endpoint to log in a user by verifying the provided credentials (email and password).
     * If successful, a JWT token is generated and returned as part of the response.
     *
     * @param request The login request containing the email and password.
     * @return A response containing the generated JWT token or an error message.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            // Authenticate the user with the provided credentials (email and password)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Set the authentication context for the current user
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate a JWT token for the authenticated user
            String jwtToken = jwtUtil.generateToken(authentication);

            // Return the JWT token as part of the response
            return ResponseEntity.ok(new AuthResponse(jwtToken));
        } catch (Exception e) {
            // Return an error response if authentication fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Endpoint to register a new user.
     * After saving the user's details (including password hashing), the user is automatically logged in
     * and a JWT token is generated and returned.
     *
     * @param request The registration request containing the user's details.
     * @return A response containing the JWT token or an error message.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserEntity request) {
        try {
            // Store the raw password temporarily to pass it to the login method after hashing
            String rawPassword = request.getPassword();

            // Save the new user in the database (password will be hashed inside saveUser())
            userService.saveUser(request);

            // Create a login request with the plain password
            LoginRequestDTO loginRequest = new LoginRequestDTO(request.getEmail(), rawPassword);

            // Call the login method to authenticate the newly registered user and generate a JWT token
            return login(loginRequest);
        } catch (Exception e) {
            // Handle any errors that occur during registration
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}

/**
 * Record to represent the response containing the generated JWT token.
 * This response is returned after successful login or registration.
 */
record AuthResponse(String token) {
}
