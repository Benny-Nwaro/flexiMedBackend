package com.example.flexiMed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for the application.
 * This class is responsible for defining Spring Beans for various components in the application.
 * Specifically, it defines a `RestTemplate` bean for making HTTP requests.
 */
@Configuration
public class AppConfig {

    /**
     * Bean definition for RestTemplate.
     * The `RestTemplate` is used to perform HTTP requests to external APIs or services.
     * By defining it as a Spring Bean, it can be injected into other components in the application.
     *
     * @return a new instance of {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
