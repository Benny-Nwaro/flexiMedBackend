package com.example.flexiMed.patients;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class PatientRecordsDTO {

    private UUID id;

    @NotNull(message = "Request ID cannot be null")
    private UUID requestId;

    @NotNull(message = "Patient ID cannot be null")
    private UUID patientId;

    @NotBlank(message = "Contact cannot be empty")
    @Pattern(regexp = "^[0-9+\\-() ]{7,15}$", message = "Invalid contact number format")
    private String contact;

    @Size(max = 1000, message = "Medical notes must not exceed 1000 characters")
    private String medicalNotes;

    // Default Constructor
    public PatientRecordsDTO() {}

    // Constructor with all fields
    public PatientRecordsDTO(UUID id, UUID requestId,
                             UUID patientId, String contact,
                             String medicalNotes) {
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

    public UUID getPatientId () {
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
