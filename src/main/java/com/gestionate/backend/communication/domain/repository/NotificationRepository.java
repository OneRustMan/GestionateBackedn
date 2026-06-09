package com.gestionate.backend.communication.domain.repository;

import com.gestionate.backend.communication.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
