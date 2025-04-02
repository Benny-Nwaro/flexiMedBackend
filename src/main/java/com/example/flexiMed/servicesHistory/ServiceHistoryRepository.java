package com.example.flexiMed.servicesHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceHistoryRepository extends JpaRepository<ServiceHistoryEntity, UUID> {
    List<ServiceHistoryEntity> findByRequest_Id(UUID requestId);

}

