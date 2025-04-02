package com.example.flexiMed.requests;

import com.example.flexiMed.ambulances.AmbulanceEntity;
import com.example.flexiMed.users.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "requests")
public class RequestEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "ambulance_id")
    private AmbulanceEntity ambulance;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Request status cannot be null")
    private RequestStatus requestStatus;

    @NotNull(message = "Request time cannot be null")
    private LocalDateTime requestTime;

    private LocalDateTime dispatchTime;
    private LocalDateTime arrivalTime;

    // Add latitude and longitude fields
    private double latitude;
    private double longitude;

    private String description;

    public RequestEntity() {
        this.requestTime = LocalDateTime.now(); // Default request time to now
    }

    public RequestEntity(UUID id, UserEntity user, AmbulanceEntity ambulance, RequestStatus requestStatus,
                         LocalDateTime requestTime, LocalDateTime dispatchTime, LocalDateTime arrivalTime,
                          double latitude, double longitude, String description) {
        this.id = id;
        this.user = user;
        this.ambulance = ambulance;
        this.requestStatus = requestStatus;
        this.requestTime = (requestTime != null) ? requestTime : LocalDateTime.now();
        this.dispatchTime = dispatchTime;
        this.arrivalTime = arrivalTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public AmbulanceEntity getAmbulance() {
        return ambulance;
    }

    public void setAmbulance(AmbulanceEntity ambulance) {
        this.ambulance = ambulance;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDateTime getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(LocalDateTime dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
