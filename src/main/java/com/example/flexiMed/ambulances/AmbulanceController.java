package com.example.flexiMed.ambulances;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ambulances")
public class AmbulanceController {
    private final AmbulanceService ambulanceService;

    public AmbulanceController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @GetMapping
    public ResponseEntity<List<AmbulanceDTO>> getAllAmbulances() {
        return ResponseEntity.ok(ambulanceService.getAllAmbulances());
    }

    @PostMapping
    public ResponseEntity<AmbulanceDTO> createAmbulance(@RequestBody AmbulanceDTO ambulanceDTO){
        return ResponseEntity.ok((ambulanceService.saveAmbulance(ambulanceDTO)));
    }
    @PutMapping("/{id}/update-location")
    public ResponseEntity<AmbulanceDTO> updateLocation(@PathVariable UUID id,
                                                       @RequestParam double latitude,
                                                       @RequestParam double longitude) {
        return ResponseEntity.ok(ambulanceService.updateLocation(id, latitude, longitude));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmbulanceDTO> updateAmbulance(
            @PathVariable UUID id,
            @RequestBody AmbulanceDTO ambulanceDTO
    ) {
        AmbulanceDTO updatedAmbulance = ambulanceService.updateAmbulance(id, ambulanceDTO.isAvailabilityStatus(),
                ambulanceDTO.getDriverName(), ambulanceDTO.getDriverContact());
        return ResponseEntity.ok(updatedAmbulance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAmbulance(@PathVariable UUID id) {
        ambulanceService.deleteAmbulance(id);
        return ResponseEntity.ok("Ambulance deleted successfully.");
    }

}
