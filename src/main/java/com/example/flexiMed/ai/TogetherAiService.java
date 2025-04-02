package com.example.flexiMed.ai;



import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class TogetherAiService {

    private final String API_URL = "https://api.together.xyz/v1/chat/completions";
    private final String API_KEY = "49ee0f754e2c9e5c0c4433b4d5fcda414afcaa4331515f3dc35b1abec79a0eb9";

    private final RestTemplate restTemplate;

    public TogetherAiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String chatWithAi(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistralai/Mixtral-8x7B-Instruct-v0.1"); // Use a valid model
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBody.put("max_tokens", 200);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);

        return response.getBody();
    }
}


