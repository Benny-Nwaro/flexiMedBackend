package com.example.flexiMed.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to set up the OpenAPI documentation for the FlexiMed API.
 * This configuration generates OpenAPI documentation that can be used by tools like Swagger UI
 * for easy access to the API endpoints, request parameters, and response formats.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Bean that configures the OpenAPI documentation settings for the FlexiMed API.
     *
     * @return An OpenAPI object configured with title, version, and description of the API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FlexiMed API")  // Set the API title
                        .version("1.0")          // Set the API version
                        .description("Flexi Medical Dispatch API")  // Set a description for the API
                );
    }
}
