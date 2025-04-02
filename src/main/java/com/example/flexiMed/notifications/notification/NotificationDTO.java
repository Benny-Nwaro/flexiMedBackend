package com.example.flexiMed.notifications.notification;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationDTO {

    private UUID id;

    @NotNull(message = "Recipient ID cannot be null")
    private UUID recipientId;

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 500, message = "Message cannot exceed 500 characters")
    private String message;

    private boolean seen;

    @NotNull(message = "Timestamp cannot be null")
    private LocalDateTime timestamp;

    public NotificationDTO() {
        this.timestamp = LocalDateTime.now(); // Default timestamp to now
    }

    public NotificationDTO(UUID id, UUID recipientId, String message, boolean seen, LocalDateTime timestamp) {
        if (recipientId == null) {
            throw new IllegalArgumentException("Recipient ID cannot be null");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        if (message.length() > 500) {
            throw new IllegalArgumentException("Message cannot exceed 500 characters");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }

        this.id = id;
        this.recipientId = recipientId;
        this.message = message;
        this.seen = seen;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        if (recipientId == null) {
            throw new IllegalArgumentException("Recipient ID cannot be null");
        }
        this.recipientId = recipientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        if (message.length() > 500) {
            throw new IllegalArgumentException("Message cannot exceed 500 characters");
        }
        this.message = message;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        this.timestamp = timestamp;
    }
}

