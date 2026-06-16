package com.gestionate.backend.communication.mapper;

import org.mapstruct.Mapper;

import com.gestionate.backend.communication.model.ContactMessage;
import com.gestionate.backend.communication.dto.ContactMessageRequest;
import com.gestionate.backend.communication.dto.ContactMessageResponse;

@Mapper(componentModel = "spring")
public interface ContactMessageMapper {
    ContactMessage toEntity(ContactMessageRequest request);

    ContactMessageResponse toResponse(ContactMessage contactMessage);
}
