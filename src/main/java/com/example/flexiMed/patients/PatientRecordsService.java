package com.example.flexiMed.patients;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientRecordsService {
    private final PatientRecordsRepository patientRecordsRepository;

    public PatientRecordsService(PatientRecordsRepository patientRecordsRepository) {
        this.patientRecordsRepository = patientRecordsRepository;
    }

    @Transactional
    public PatientRecordsDTO addPatientRecord(PatientRecordsDTO patientRecordDTO) {
        UUID patientId = patientRecordDTO.getPatientId();
        List<PatientRecordsEntity> existingRecords = patientRecordsRepository.findByPatientId(patientId);

        if (!existingRecords.isEmpty()) {
            // Update the first matching record
            PatientRecordsEntity existingEntity = existingRecords.get(0); // Assuming you want to update the first one
            PatientRecordsDTO updatedRecord = new PatientRecordsDTO(
                    existingEntity.getId(),
                    patientRecordDTO.getRequestId(),
                    patientRecordDTO.getPatientId(),
                    patientRecordDTO.getContact(),
                    patientRecordDTO.getMedicalNotes()
            );

            return updatePatientRecord(existingEntity.getId(), updatedRecord);
        } else {
            // Create a new record
            PatientRecordsEntity entity = new PatientRecordsEntity(
                    null,
                    patientRecordDTO.getRequestId(),
                    patientRecordDTO.getPatientId(),
                    patientRecordDTO.getContact(),
                    patientRecordDTO.getMedicalNotes()
            );
            try {
                PatientRecordsEntity savedEntity = patientRecordsRepository.save(entity);
                return mapToDTO(savedEntity);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to save patient record", e);
            }
        }
    }




    public PatientRecordsDTO getPatientRecordById(UUID id) {
        PatientRecordsEntity entity = patientRecordsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient record not found"));

        return mapToDTO(entity);
    }

    public List<PatientRecordsDTO> getPatientRecordsByPatientId(UUID patientId) {
        List<PatientRecordsEntity> records = patientRecordsRepository.findByPatientId(patientId);

        if (records.isEmpty()) {
            throw new RuntimeException("No patient records found for patientId: " + patientId);
        }

        return records.stream().map(this::mapToDTO).collect(Collectors.toList());
    }


    @Transactional
    public PatientRecordsDTO updatePatientRecord(UUID id, PatientRecordsDTO updatedRecord) {
        PatientRecordsEntity entity = patientRecordsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient record not found"));

        if (updatedRecord.getPatientId() != null) entity.setPatientId(updatedRecord.getPatientId());
        if (updatedRecord.getContact() != null) entity.setContact(updatedRecord.getContact());
        if (updatedRecord.getMedicalNotes() != null) entity.setMedicalNotes(updatedRecord.getMedicalNotes());

        PatientRecordsEntity savedEntity = patientRecordsRepository.save(entity);
        return mapToDTO(savedEntity);
    }


    @Transactional
    public void deletePatientRecord(UUID id) {
        if (!patientRecordsRepository.existsById(id)) {
            throw new RuntimeException("Patient record not found");
        }
        patientRecordsRepository.deleteById(id);
    }

    private PatientRecordsDTO mapToDTO(PatientRecordsEntity entity) {
        return new PatientRecordsDTO(
                entity.getId(),
                entity.getRequestId(),
                entity.getPatientId(),
                entity.getContact(),
                entity.getMedicalNotes()
        );
    }
}
