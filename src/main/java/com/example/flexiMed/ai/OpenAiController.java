package com.example.flexiMed.ai;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class OpenAiController {

    private final TogetherAiService togetherAiService;

    public OpenAiController(TogetherAiService togetherAiService) {
        this.togetherAiService = togetherAiService;
    }

    @GetMapping("/chat")
    public String chatWithAi(@RequestParam String prompt) {
        return togetherAiService.chatWithAi(prompt);
    }
}
