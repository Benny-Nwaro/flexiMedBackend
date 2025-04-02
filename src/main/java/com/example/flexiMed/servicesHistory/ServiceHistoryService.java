package com.example.flexiMed.servicesHistory;

import com.example.flexiMed.requests.RequestEntity;
import com.example.flexiMed.requests.RequestRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceHistoryService {

    private final ServiceHistoryRepository serviceHistoryRepository;
    private final RequestRepository requestRepository;

    public ServiceHistoryService(ServiceHistoryRepository serviceHistoryRepository, RequestRepository requestRepository) {
        this.serviceHistoryRepository = serviceHistoryRepository;
        this.requestRepository = requestRepository;
    }

    public ServiceHistoryDTO logEvent(ServiceHistoryDTO serviceHistory) {
        RequestEntity requestEntity = requestRepository.findById(serviceHistory.getRequestId())
                .orElseThrow(() -> new RuntimeException("Request with ID " + serviceHistory.getRequestId() + " not found"));

        ServiceHistoryEntity serviceEntity = ServiceHistoryMapper.toEntity(serviceHistory, requestEntity);
        ServiceHistoryEntity savedEntity = serviceHistoryRepository.save(serviceEntity);

        return ServiceHistoryMapper.toDTO(savedEntity);
    }

    public List<ServiceHistoryDTO> getHistoryByRequestId(UUID requestId) {
        List<ServiceHistoryEntity> historyEntities = serviceHistoryRepository.findByRequest_Id(requestId);

        if (historyEntities.isEmpty()) {
            throw new RuntimeException("No service history found for request ID: " + requestId);
        }

        return historyEntities.stream()
                .map(ServiceHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}
