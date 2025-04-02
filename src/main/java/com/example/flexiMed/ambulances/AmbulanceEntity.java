package com.example.flexiMed.ambulances;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ambulances")
public class AmbulanceEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Plate number cannot be empty")
    @Size(max = 15, message = "Plate number must not exceed 15 characters")
    private String plateNumber;

    @Column(nullable = false)
    @NotNull(message = "Latitude cannot be null")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;

    @Column(nullable = false)
    @NotNull(message = "Longitude cannot be null")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;

    private boolean availabilityStatus;

    @NotBlank(message = "Driver name cannot be empty")
    @Size(max = 100, message = "Driver name must not exceed 100 characters")
    private String driverName;

    @NotBlank(message = "Driver contact cannot be empty")
    @Pattern(regexp = "^[0-9+\\-() ]{7,15}$", message = "Invalid contact number format")
    private String driverContact;

    @PastOrPresent(message = "Last updated time cannot be in the future")
    private LocalDateTime lastUpdatedAt;

    @Version // Enables optimistic locking
    private Long version;

    public AmbulanceEntity() {
    }

    public AmbulanceEntity(UUID id, String plateNumber, Double latitude, Double longitude,
                           boolean availabilityStatus, String driverName,
                           String driverContact, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.availabilityStatus = availabilityStatus;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
