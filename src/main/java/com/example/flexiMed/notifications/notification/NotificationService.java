package com.example.flexiMed.notifications.notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendAmbulanceDispatchNotification(UUID userId, String message) {
        // Send notification directly to the user
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
    }
}
