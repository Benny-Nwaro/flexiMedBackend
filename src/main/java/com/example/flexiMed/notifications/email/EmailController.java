package com.example.flexiMed.notifications.email;

import org.springframework.web.bind.annotation.*;
import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String text,
                            @RequestParam Context context
    ) {
        try {
            emailService.sendEmail(to, subject, text, context);
            return "Email sent successfully!";
        } catch (MessagingException e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}

