package com.example.flexiMed.servicesHistory;

import com.example.flexiMed.requests.RequestEntity;
import org.springframework.stereotype.Component;

@Component
public class ServiceHistoryMapper {

    public static ServiceHistoryDTO toDTO(ServiceHistoryEntity history) {
        if (history == null) {
            return null;
        }
        return new ServiceHistoryDTO(
                history.getId(),
                history.getRequest().getId(),
                history.getEventType().name(),
                history.getEventTime(),
                history.getDetails()
        );
    }

    public static ServiceHistoryEntity toEntity(ServiceHistoryDTO dto, RequestEntity request) {
        if (dto == null) {
            return null;
        }
        ServiceHistoryEntity history = new ServiceHistoryEntity();
        history.setId(dto.getId());
        history.setRequest(request);
        history.setEventType(EventType.valueOf(dto.getEventType()));
        history.setEventTime(dto.getEventTime());
        history.setDetails(dto.getDetails());
        return history;
    }
}
