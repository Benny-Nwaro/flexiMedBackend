package com.example.flexiMed.notifications.notification;


public class NotificationMapper {

    public static NotificationDTO toDTO(NotificationEntity notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getRecipient().getUserId(),
                notification.getMessage(),
                notification.isSeen(),
                notification.getTimestamp()
        );
    }

    public static NotificationEntity toEntity(NotificationDTO notificationDTO) {
        NotificationEntity notification = new NotificationEntity();
        notification.setId(notificationDTO.getId());
        notification.setMessage(notificationDTO.getMessage());
        notification.setSeen(notificationDTO.isSeen());
        notification.setTimestamp(notificationDTO.getTimestamp());
        return notification;
    }
}

