package com.gestionate.backend.communication.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionate.backend.communication.domain.model.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

}
