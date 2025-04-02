package com.example.flexiMed.ambulances;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AmbulanceLocationController {
    private final AmbulanceLocationHandler webSocketHandler;

    public AmbulanceLocationController(AmbulanceLocationHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping("/update-location")
    public void updateLocation(@RequestBody Map<String, String> location) {
        String ambulanceId = location.get("ambulanceId");
        String userId = location.get("userId"); // The user who requested this ambulance
        String lat = location.get("lat");
        String lng = location.get("lng");

        // Create JSON payload
        Map<String, String> data = new HashMap<>();
        data.put("ambulanceId", ambulanceId);
        data.put("lat", lat);
        data.put("lng", lng);

        // Send update to only the requesting user
        webSocketHandler.sendLocationToUser(userId, data.toString());
    }
}

