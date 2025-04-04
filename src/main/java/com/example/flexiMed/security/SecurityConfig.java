package com.example.flexiMed.security;

import com.example.flexiMed.service.oauth.CustomOAuth2UserService;
import com.example.flexiMed.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * This configuration class sets up the security settings for the application,
 * including JWT authentication, OAuth2 login, CORS configuration, and user authentication.
 */
@Configuration
public class SecurityConfig {

    // Dependencies injected through constructor
    private final JwtUtil jwtUtil;  // Utility class for JWT handling
    private final UserService userService;  // Service for loading users from the database
    private final CustomOAuth2UserService customOAuth2UserService;  // Custom OAuth2 user service
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;  // Handler for successful OAuth2 login

    /**
     * Constructor to inject dependencies into the configuration class.
     */
    public SecurityConfig(JwtUtil jwtUtil, UserService userService, CustomOAuth2UserService customOAuth2UserService,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    /**
     * Bean to configure JWT Authentication Filter.
     * This filter validates the JWT token in requests to secure endpoints.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userService);
    }

    /**
     * Bean to configure UserDetailsService. This service is used to load user details from the database by email.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userService;  // UserService is used to load user details
    }

    /**
     * Bean to configure OAuth2 Authorization Request Repository.
     * This is used to store OAuth2 login requests during the authorization process.
     */
    @Bean
    public HttpSessionOAuth2AuthorizationRequestRepository authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    /**
     * Main security configuration method. It configures HTTP security settings such as CORS, CSRF, URL patterns, and OAuth2 login.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF protection
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Enable CORS with custom configuration
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(getPublicUrls().toArray(new String[0])).permitAll()  // Public URLs (no authentication required)
                        .anyRequest().authenticated()  // Require authentication for all other requests
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))  // Custom user info service for OAuth2 login
                        .successHandler(oAuth2LoginSuccessHandler)  // Custom success handler for OAuth2 login
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before UsernamePasswordAuthenticationFilter

        return http.build();
    }

    /**
     * Bean to configure CORS settings. It defines allowed origins, methods, headers, and credentials.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:3000", "http://127.0.0.1:3000", "https://flexi-med-front-itcp.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);  // Allow credentials (cookies, authentication headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // Apply CORS configuration to all endpoints
        return source;
    }

    /**
     * Helper method to retrieve a list of public URLs that should not require authentication.
     */
    private List<String> getPublicUrls() {
        return List.of(
                "/",
                "/api/auth/register",  // Public URL for user registration
                "/api/auth/login",  // Public URL for login
                "/api/auth/oauth2",  // Public URL for OAuth2 authorization
                "/oauth2/authorization/**",  // Public OAuth2 authorization URL
                "/login/oauth2/**",  // Public login callback URL
                "/swagger-ui/**",  // Public Swagger UI documentation URL
                "/v3/api-docs/**",  // Public API documentation URL
                "/swagger-ui.html",  // Public Swagger UI HTML
                "/v3/api-docs.yaml",  // Public API documentation in YAML format
                "/favicon.ico",  // Public favicon
                "/uploads/**",  // Public file uploads
                "/api/openai/**",  // Public OpenAI API endpoints
                "/ws/**"  // Public WebSocket endpoint
        );
    }

    /**
     * Bean to configure the Authentication Provider.
     * This provider is responsible for authenticating users by their credentials (email and password).
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());  // UserDetailsService is used to load user details
        authProvider.setPasswordEncoder(passwordEncoder());  // Password encoder to hash and verify passwords
        return authProvider;
    }

    /**
     * Bean to configure the Authentication Manager.
     * This manager is responsible for managing the authentication flow (authenticating users based on their credentials).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();  // Get the authentication manager from the configuration
    }

    /**
     * Bean to configure the Password Encoder.
     * This encoder is used to hash and verify passwords using BCrypt encryption.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Using BCrypt for password encryption
    }
}
