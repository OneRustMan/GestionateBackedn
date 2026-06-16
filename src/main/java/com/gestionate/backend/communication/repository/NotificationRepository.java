package com.gestionate.backend.communication.repository;

import com.gestionate.backend.communication.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
