package com.example.flexiMed.notifications.sms;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sms")
public class SMSController {
    private final SMSService smsService;

    public SMSController(SMSService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public String sendSms(@RequestParam String to, @RequestParam String message) {
        try {
            smsService.sendSms(to, message);
            return "SMS sent successfully!";
        } catch (Exception e) {
            return "Failed to send SMS: " + e.getMessage();
        }
    }
}

