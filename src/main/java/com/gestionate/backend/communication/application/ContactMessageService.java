package com.gestionate.backend.communication.application;

import org.springframework.stereotype.Service;

import com.gestionate.backend.communication.domain.model.ContactMessage;
import com.gestionate.backend.communication.domain.repository.ContactMessageRepository;
import com.gestionate.backend.communication.infrastructure.mapping.ContactMessageMapper;
import com.gestionate.backend.communication.interfaces.rest.dto.ContactMessageRequest;
import com.gestionate.backend.communication.interfaces.rest.dto.ContactMessageResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ContactMessageService implements IContactMessageService {
    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;

    @Override
    @Transactional
    public ContactMessageResponse sendMessage(ContactMessageRequest request) {
        ContactMessage contactMessage = ContactMessage.builder()
                .name(request.name()).email(request.email()).subject(request.subject()).message(request.message())
                .attended(false)
                .created_at(LocalDate.now()).build();
        contactMessage = contactMessageRepository.save(contactMessage);
        return contactMessageMapper.toResponse(contactMessage);
    }
}
