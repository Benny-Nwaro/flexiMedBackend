package com.example.flexiMed.notifications.notification;

import com.example.flexiMed.users.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;  // Use UUID for ID

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserEntity recipient;  // Reference to User entity

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 500, message = "Message cannot exceed 500 characters")
    private String message;

    private boolean seen = false;
    private LocalDateTime timestamp = LocalDateTime.now();

    public NotificationEntity() {}

    public NotificationEntity(UserEntity recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotBlank(message = "Recipient cannot be empty") @Size(max = 100, message = "Recipient name cannot exceed 100 characters")
    UserEntity getRecipient() {
        return recipient;
    }

    public void setRecipient
            (@NotBlank(message = "Recipient cannot be empty")
             @Size(max = 100, message = "Recipient name cannot exceed 100 characters")
             UserEntity recipient) { this.recipient = recipient; }

    public @NotBlank(message = "Message cannot be empty")
    @Size(max = 500, message = "Message cannot exceed 500 characters")
    String getMessage() {
        return message;
    }

    public void setMessage(@NotBlank(message = "Message cannot be empty")
                           @Size(max = 500, message = "Message cannot exceed 500 characters")
                           String message) { this.message = message; }

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
        this.timestamp = timestamp;
    }
}
