package com.example.flexiMed.patients;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "patient_records")
public class PatientRecordsEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "request_id", nullable = false) // Store only the UUID
    @NotNull(message = "Request ID cannot be null")
    private UUID requestId;

    @Column(name = "patient_id", nullable = false) // Store only the UUID
    @NotNull(message = "patient_id cannot be null") // Links to Users table
    private UUID patientId;

    @NotBlank(message = "Contact cannot be empty")
    @Pattern(regexp = "^[0-9+\\-() ]{7,15}$", message = "Invalid contact number format")
    private String contact;

    @Size(max = 1000, message = "Medical notes must not exceed 1000 characters")
    private String medicalNotes;

    // Default Constructor (Required for JPA)
    public PatientRecordsEntity() {}

    // Constructor with all fields
    public PatientRecordsEntity(UUID id, UUID requestId,
                                UUID patientId, String contact, String medicalNotes) {
        this.id = id;
        this.requestId = requestId;
        this.patientId = patientId;
        this.contact = contact;
        this.medicalNotes = medicalNotes;
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

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(String medicalNotes) {
        this.medicalNotes = medicalNotes;
    }
}
