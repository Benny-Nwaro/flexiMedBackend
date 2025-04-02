package com.example.flexiMed.ambulances;

import com.example.flexiMed.requests.RequestRepository;
import com.example.flexiMed.requests.RequestStatus;
import jakarta.transaction.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AmbulanceService {
    private final AmbulanceRepository ambulanceRepository;
    private final RequestRepository requestRepository;



    public AmbulanceService(AmbulanceRepository ambulanceRepository, RequestRepository requestRepository) {
        this.ambulanceRepository = ambulanceRepository;
        this.requestRepository = requestRepository;
    }

    public AmbulanceDTO saveAmbulance(AmbulanceDTO ambulance) {
      try{
          AmbulanceEntity newAmbulance = AmbulanceMapper.toEntity(ambulance);

          return AmbulanceMapper.toDTO(ambulanceRepository.save(newAmbulance));
      }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Ambulance record was modified by another transaction. Please retry.", e);
        }
    }

    public List<AmbulanceDTO> getAllAmbulances() {
        return ambulanceRepository.findAll().stream().map(AmbulanceMapper::toDTO).toList();
    }

    @Transactional
    public AmbulanceDTO updateAmbulance(UUID id, boolean availabilityStatus, String driverName, String driverContact) {
        return ambulanceRepository.findById(id)
                .map(ambulance -> {
                    ambulance.setAvailabilityStatus(availabilityStatus);
                    ambulance.setDriverName(driverName);
                    ambulance.setDriverContact(driverContact);
                    ambulance.setLastUpdatedAt(LocalDateTime.now());
                    return AmbulanceMapper.toDTO(ambulanceRepository.save(ambulance));
                })
                .orElseThrow(() -> new RuntimeException("Ambulance not found with ID: " + id));
    }

    @Transactional
    public void deleteAmbulance(UUID id) {
        // Use requestService to check for incomplete requests
        if (id == null) {
            throw new IllegalArgumentException("Ambulance ID cannot be null");
        }

        long count = requestRepository.countByAmbulance_IdAndRequestStatusNot(id, RequestStatus.COMPLETED);
        if(count > 0){
            ambulanceRepository.deleteById(id);
        }

    }

    @Transactional
    public AmbulanceDTO updateLocation(UUID id, double latitude, double longitude) {
        return ambulanceRepository.findById(id)
                .map(ambulance -> {
                    ambulance.setLatitude(latitude);
                    ambulance.setLongitude(longitude);
                    ambulance.setLastUpdatedAt(LocalDateTime.now());
                    return AmbulanceMapper.toDTO(ambulanceRepository.save(ambulance));
                })
                .orElseThrow(() -> new RuntimeException("Ambulance not found with ID: " + id));
    }


    public AmbulanceDTO findClosestAmbulance(double userLatitude, double userLongitude) {
        List<AmbulanceEntity> availableAmbulances = ambulanceRepository.findByavailabilityStatusIsTrue();

        if (availableAmbulances.isEmpty()) {
            throw new RuntimeException("No available ambulances at the moment");
        }

        // Find the closest ambulance
        Optional<AmbulanceEntity> closestAmbulance = availableAmbulances.stream()
                .min((a1, a2) -> Double.compare(
                        calculateDistance(userLatitude, userLongitude, a1.getLatitude(), a1.getLongitude()),
                        calculateDistance(userLatitude, userLongitude, a2.getLatitude(), a2.getLongitude())
                ));

        return closestAmbulance
                .map(AmbulanceMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Could not find a suitable ambulance"));
    }

    // Haversine formula for distance calculation (great-circle distance)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in km
    }

}