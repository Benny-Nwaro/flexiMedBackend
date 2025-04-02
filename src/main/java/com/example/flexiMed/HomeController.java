package com.example.flexiMed;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome! <a href='/oauth2/authorization/google'>Login with Google</a>";
    }

    @GetMapping("/user")
    public OAuth2User getUser(@AuthenticationPrincipal OAuth2User user) {
        return user;
    }
}

