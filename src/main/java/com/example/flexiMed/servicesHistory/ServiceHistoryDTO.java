package com.example.flexiMed.servicesHistory;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class ServiceHistoryDTO {

    private UUID id;

    @NotNull(message = "Request ID cannot be null")
    private UUID requestId;

    @NotNull(message = "Event type cannot be null")
    private String eventType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;

    @Size(max = 500, message = "Details must not exceed 500 characters")
    private String details;

    // No-Args Constructor (required for frameworks like Jackson)
    public ServiceHistoryDTO() {}

    // All-Args Constructor
    public ServiceHistoryDTO(UUID id, UUID requestId, String eventType,
                             LocalDateTime eventTime, String details) {
        this.id = id;
        this.requestId = requestId;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.details = details;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
