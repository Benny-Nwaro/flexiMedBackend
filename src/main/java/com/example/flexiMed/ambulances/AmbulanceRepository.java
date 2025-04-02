package com.example.flexiMed.ambulances;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AmbulanceRepository extends JpaRepository<AmbulanceEntity, UUID> {
    Optional<AmbulanceEntity> findFirstByavailabilityStatusIsTrue();
    List<AmbulanceEntity> findByavailabilityStatusIsTrue();


}

