package com.example.flexiMed.patients;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientRecordsController {
    private final PatientRecordsService patientRecordsService;

    public PatientRecordsController(PatientRecordsService patientRecordsService) {
        this.patientRecordsService = patientRecordsService;
    }

    @PostMapping
    public ResponseEntity<PatientRecordsDTO> addPatient(@RequestBody PatientRecordsDTO patientDTO) {
        return ResponseEntity.ok(patientRecordsService.addPatientRecord(patientDTO));
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientRecordsDTO>> getPatientRecordsByPatientId(@PathVariable UUID patientId) {
        List<PatientRecordsDTO> records = patientRecordsService.getPatientRecordsByPatientId(patientId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientRecordsDTO> getPatientRecordById(@PathVariable UUID id) {
        PatientRecordsDTO record = patientRecordsService.getPatientRecordById(id);
        return ResponseEntity.ok(record);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientRecordsDTO> updatePatientRecord(
            @PathVariable UUID id,
            @RequestBody PatientRecordsDTO updatedRecord) {

        PatientRecordsDTO updated = patientRecordsService.updatePatientRecord(id, updatedRecord);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatientRecord(@PathVariable UUID id) {
        patientRecordsService.deletePatientRecord(id);
        return ResponseEntity.ok("Patient record deleted successfully.");
    }
}
