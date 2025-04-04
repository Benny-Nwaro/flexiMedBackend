package com.example.flexiMed.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for handling JWT (JSON Web Token) operations.
 * This class provides methods for generating, validating, and extracting information from JWT tokens.
 */
@Component
public class JwtUtil {

    private final Key key; // Secret key for signing and verifying JWT tokens

    /**
     * Constructor that initializes the JwtUtil class with a secret key for signing JWT tokens.
     *
     * @param secretKey The secret key used for signing JWT tokens, injected from application properties.
     */
    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decode the Base64-encoded secret key
        this.key = Keys.hmacShaKeyFor(keyBytes); // Generate the key using HMAC SHA algorithm
    }

    /**
     * Generates a JWT token based on the provided Authentication object.
     * The token is signed with a secret key and includes user information such as username, issue date, and expiration.
     *
     * @param authentication The Authentication object containing the user's details (username).
     * @return The generated JWT token as a string.
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // Get user details from authentication
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Set the subject of the token (usually the username)
                .setIssuedAt(new Date()) // Set the issue date of the token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Set the expiration time (10 hours)
                .signWith(key) // Sign the token with the secret key
                .compact(); // Return the token as a compact string
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token The JWT token.
     * @return The username (subject) from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Extract the username (subject) from the token
    }

    /**
     * Extracts the expiration date of the JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extract the expiration date from the token
    }

    /**
     * Extracts a specific claim from the JWT token.
     * The claim is a part of the payload of the token (e.g., username, expiration).
     *
     * @param token The JWT token.
     * @param claimsResolver The function used to extract the specific claim from the token.
     * @param <T> The type of the claim to extract.
     * @return The extracted claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()  // Parse the JWT token
                .setSigningKey(key) // Use the secret key to verify the token's signature
                .build()
                .parseClaimsJws(token) // Parse the JWT and extract claims
                .getBody(); // Extract the claims body (payload)
        return claimsResolver.apply(claims); // Apply the claim resolver function to extract the desired claim
    }

    /**
     * Validates the JWT token by checking if the username from the token matches the provided UserDetails
     * and if the token is not expired.
     *
     * @param token The JWT token.
     * @param userDetails The UserDetails object containing the user information.
     * @return True if the token is valid (username matches and token is not expired), false otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token); // Validate token
    }

    /**
     * Checks if the JWT token has expired by comparing the expiration date to the current date.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Check if expiration date is in the past
    }
}
