package com.example.flexiMed.ambulances;

import org.springframework.stereotype.Component;

@Component
public class AmbulanceMapper {

    public static AmbulanceDTO toDTO(AmbulanceEntity ambulance) {
        if (ambulance == null) {
            return null;
        }
        return new AmbulanceDTO(
                ambulance.getId(),
                ambulance.getPlateNumber(),
                ambulance.getLatitude(),
                ambulance.getLongitude(),
                ambulance.isAvailabilityStatus(),
                ambulance.getDriverName(),
                ambulance.getDriverContact(),
                ambulance.getLastUpdatedAt()
        );
    }

    public static AmbulanceEntity toEntity(AmbulanceDTO dto) {
        if (dto == null) {
            return null;
        }
        AmbulanceEntity ambulance = new AmbulanceEntity();
        ambulance.setId(dto.getId());
        ambulance.setPlateNumber(dto.getPlateNumber());
        ambulance.setLatitude(dto.getLatitude());
        ambulance.setLongitude(dto.getLongitude());
        ambulance.setAvailabilityStatus(dto.isAvailabilityStatus());
        ambulance.setDriverName(dto.getDriverName());
        ambulance.setDriverContact(dto.getDriverContact());
        ambulance.setLastUpdatedAt(dto.getLastUpdatedAt());
        return ambulance;
    }
}
