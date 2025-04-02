package com.example.flexiMed.requests;

import com.example.flexiMed.ambulances.AmbulanceEntity;
import com.example.flexiMed.users.UserEntity;

public class RequestMapper {

    // Mapping from RequestEntity to RequestDTO
    public static RequestDTO toDTO(RequestEntity request) {
        if (request == null) {
            return null;
        }
        return new RequestDTO(
                request.getId(),
                request.getUser() != null ? request.getUser().getUserId() : null, // Ensure user is not null
                request.getAmbulance() != null ? request.getAmbulance().getId() : null,
                request.getRequestStatus() != null ? request.getRequestStatus() : null,
                request.getRequestTime(),
                request.getDispatchTime(),
                request.getArrivalTime(),
                request.getLatitude(),
                request.getLongitude(),
                request.getDescription()
        );
    }

    // Mapping from RequestDTO to RequestEntity
    public static RequestEntity toEntity(RequestDTO dto, UserEntity user, AmbulanceEntity ambulance) {
        if (dto == null) {
            return null;
        }
        RequestEntity request = new RequestEntity();
        request.setId(dto.getId());
        request.setUser(user); // Ensure user is valid before mapping
        request.setAmbulance(ambulance); // Ensure ambulance is valid before mapping
        if (dto.getRequestStatus() != null) {
            try {
                request.setRequestStatus(RequestStatus.valueOf(dto.getRequestStatus().toString()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Request Status: " + dto.getRequestStatus(), e);
            }
        }
        request.setRequestTime(dto.getRequestTime());
        request.setDispatchTime(dto.getDispatchTime());
        request.setArrivalTime(dto.getArrivalTime());
        request.setLatitude(dto.getLatitude());
        request.setLongitude(dto.getLongitude());
        request.setDescription(dto.getDescription());
        return request;
    }
}
