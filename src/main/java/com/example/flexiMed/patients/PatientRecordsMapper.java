package com.example.flexiMed.patients;

import org.springframework.stereotype.Component;

@Component
public class PatientRecordsMapper {

    public static PatientRecordsDTO toDTO(PatientRecordsEntity patient) {
        if (patient == null) {
            return null;
        }
        return new PatientRecordsDTO(  // ✅ Return a DTO, not an Entity
                patient.getId(),
                patient.getRequestId(), // Extract only the request ID
                patient.getPatientId(),
                patient.getContact(),
                patient.getMedicalNotes()
        );
    }

    public static PatientRecordsEntity toEntity(PatientRecordsDTO dto) {
        if (dto == null) {
            return null;
        }
        PatientRecordsEntity patient = new PatientRecordsEntity();
        patient.setId(dto.getId());
        patient.setRequestId(dto.getRequestId());  // Directly setting the UUID
        patient.setPatientId(dto.getPatientId());
        patient.setContact(dto.getContact());
        patient.setMedicalNotes(dto.getMedicalNotes());
        return patient;
    }

}
