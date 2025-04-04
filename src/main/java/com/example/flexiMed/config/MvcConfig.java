package com.example.flexiMed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration class for customizing Spring MVC resource handling.
 * This configuration allows serving static resources, such as files stored in a specific directory.
 * Specifically, it handles the `/uploads/**` URL pattern to serve files from the `uploads` directory.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Method to add custom resource handlers for static resources.
     * In this case, it serves files located in the `uploads` directory to clients who access
     * URLs with the `/uploads/**` pattern.
     *
     * @param registry The {@link ResourceHandlerRegistry} object to register custom resource handlers.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Define the upload directory and its absolute path
        Path uploadDir = Paths.get("uploads");
        String uploadPath = uploadDir.toAbsolutePath().toUri().toString();

        // Register the resource handler for `/uploads/**` URL pattern
        // It maps the pattern to the upload directory's path
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}
