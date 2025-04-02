package com.example.flexiMed.notifications.notification;

import com.example.flexiMed.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

//    // Find all notifications for a specific user
//    List<NotificationEntity> findByRecipientId(UUID recipientId);
//
//    // Find all unseen notifications for a specific user
//    List<NotificationEntity> findByRecipientIdAndSeenFalse(UUID recipientId);
//    // Get all notifications for a specific user
//    List<NotificationEntity> findByRecipient(UserEntity recipient);

    // Get all unseen notifications for a specific user
    List<NotificationEntity> findByRecipientAndSeenFalse(UserEntity recipient);
}

