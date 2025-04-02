package com.example.flexiMed.servicesHistory;

import com.example.flexiMed.requests.RequestEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_history")
public class ServiceHistoryEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    @NotNull(message = "Request cannot be null")
    private RequestEntity request;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Event type cannot be null")
    private EventType eventType;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime eventTime;

    @Column(length = 500)
    @Size(max = 500, message = "Details must not exceed 500 characters")
    private String details;

    public ServiceHistoryEntity() {}

    public ServiceHistoryEntity(UUID id, RequestEntity request, EventType eventType, LocalDateTime eventTime, String details) {
        this.id = id;
        this.request = request;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.details = details;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RequestEntity getRequest() {
        return request;
    }

    public void setRequest(RequestEntity request) {
        this.request = request;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
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
