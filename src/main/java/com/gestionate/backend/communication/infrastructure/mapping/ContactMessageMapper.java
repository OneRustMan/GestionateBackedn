package com.gestionate.backend.communication.infrastructure.mapping;

import org.mapstruct.Mapper;

import com.gestionate.backend.communication.domain.model.ContactMessage;
import com.gestionate.backend.communication.interfaces.rest.dto.ContactMessageRequest;
import com.gestionate.backend.communication.interfaces.rest.dto.ContactMessageResponse;

@Mapper(componentModel = "spring")
public interface ContactMessageMapper {
    ContactMessage toEntity(ContactMessageRequest request);

    ContactMessageResponse toResponse(ContactMessage contactMessage);
}
