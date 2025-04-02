package com.example.flexiMed.requests;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requests")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(@RequestBody RequestDTO request) {
        System.out.println("incoming  request ----> " + request.toString());
        return ResponseEntity.ok(requestService.createRequest(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RequestDTO>> getUserRequests(@PathVariable UUID userId) {
        List<RequestDTO> requests = requestService.getUserRequests(userId);
        return ResponseEntity.ok(requests);
    }
    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAllRequests() {
        List<RequestDTO> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}/dispatch")
    public ResponseEntity<RequestDTO> dispatchAmbulance(@PathVariable UUID id, @RequestParam UUID ambulanceId) {
        RequestDTO response = requestService.dispatchAmbulance(id, ambulanceId);
        return ResponseEntity.ok(response);
    }
}
