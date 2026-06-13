package com.gestionate.backend.communication.application;

import com.gestionate.backend.communication.domain.model.ContactMessage;
import com.gestionate.backend.communication.domain.repository.ContactMessageRepository;
import com.gestionate.backend.communication.infrastructure.mapping.ContactMessageMapper;
import com.gestionate.backend.communication.interfaces.rest.dto.ContactMessageRequest;
import com.gestionate.backend.communication.interfaces.rest.dto.ContactMessageResponse;
import com.gestionate.backend.iam.application.IEmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactMessageService implements IContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;
    private final IEmailService emailService;

    @Value("${app.frontend.register-url}")
    private String registerUrl;

    @Override
    @Transactional
    public ContactMessageResponse sendMessage(ContactMessageRequest request) {
        ContactMessage contactMessage = ContactMessage.builder()
                .name(request.name())
                .email(request.email())
                .subject(request.subject())
                .message(request.message())
                .attended(false)
                .created_at(LocalDate.now())
                .build();

        contactMessage = contactMessageRepository.save(contactMessage);

        try {
            emailService.sendContactAutoReply(
                    contactMessage.getEmail(),
                    contactMessage.getName(),
                    registerUrl);

            contactMessage.setAttended(true);
            contactMessage = contactMessageRepository.save(contactMessage);

        } catch (Exception exception) {
            log.warn(
                    "No se pudo enviar el correo automático de contáctenos a {}. Motivo: {}",
                    contactMessage.getEmail(),
                    exception.getMessage());
        }

        return contactMessageMapper.toResponse(contactMessage);
    }
}
