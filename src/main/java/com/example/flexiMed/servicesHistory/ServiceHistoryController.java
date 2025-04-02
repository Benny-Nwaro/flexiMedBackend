package com.example.flexiMed.servicesHistory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/service-history")
public class ServiceHistoryController {
    private final ServiceHistoryService serviceHistoryService;

    public ServiceHistoryController(ServiceHistoryService serviceHistoryService) {
        this.serviceHistoryService = serviceHistoryService;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<List<ServiceHistoryDTO>> getHistoryByRequest(@PathVariable UUID requestId) {
        return ResponseEntity.ok(serviceHistoryService.getHistoryByRequestId(requestId));
    }
}
