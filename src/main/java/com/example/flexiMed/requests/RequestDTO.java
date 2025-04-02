package com.example.flexiMed.requests;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestDTO {

    private UUID id;
    private UUID userId;
    private UUID ambulanceId;
    private RequestStatus requestStatus;
    private LocalDateTime requestTime;
    private LocalDateTime dispatchTime;
    private LocalDateTime arrivalTime;
    private double latitude;
    private double longitude;
    private String description;

    public RequestDTO() {}

    public RequestDTO(UUID id, UUID userId, UUID ambulanceId,
                      RequestStatus requestStatus, LocalDateTime requestTime,
                      LocalDateTime dispatchTime, LocalDateTime arrivalTime,
                      double latitude, double longitude, String description) {
        this.id = id;
        this.userId = validateUUID(userId, "User ID");
        this.ambulanceId = ambulanceId;
        this.requestStatus = validateEnum(requestStatus, "Request Status");
        this.requestTime = validateNotNull(requestTime, "Request Time");
        this.dispatchTime = validateFutureOrNull(dispatchTime, requestTime, "Dispatch Time");
        this.arrivalTime = validateFutureOrNull(arrivalTime, requestTime, "Arrival Time");
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = validateString(description, "Description");
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = validateUUID(id, "Request ID"); }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = validateUUID(userId, "User ID"); }

    public UUID getAmbulanceId() { return ambulanceId; }
    public void setAmbulanceId(UUID ambulanceId) { this.ambulanceId = ambulanceId; }

    public RequestStatus getRequestStatus() { return requestStatus; }
    public void setRequestStatus(RequestStatus requestStatus) { this.requestStatus = validateEnum(requestStatus, "Request Status"); }

    public LocalDateTime getRequestTime() { return requestTime; }
    public void setRequestTime(LocalDateTime requestTime) { this.requestTime = validateNotNull(requestTime, "Request Time"); }

    public LocalDateTime getDispatchTime() { return dispatchTime; }
    public void setDispatchTime(LocalDateTime dispatchTime) { this.dispatchTime = validateFutureOrNull(dispatchTime, requestTime, "Dispatch Time"); }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = validateFutureOrNull(arrivalTime, requestTime, "Arrival Time"); }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = validateString(description, "Description"); }

    private UUID validateUUID(UUID value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
        return value;
    }

    private String validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    private LocalDateTime validateNotNull(LocalDateTime value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
        return value;
    }

    private LocalDateTime validateFutureOrNull(LocalDateTime value, LocalDateTime reference, String fieldName) {
        if (value != null && reference != null && value.isBefore(reference)) {
            throw new IllegalArgumentException(fieldName + " cannot be before request time.");
        }
        return value;
    }

    private RequestStatus validateEnum(RequestStatus value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
        return value;
    }
}
