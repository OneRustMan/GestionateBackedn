package com.gestionate.backend.communication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionate.backend.communication.model.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

}
