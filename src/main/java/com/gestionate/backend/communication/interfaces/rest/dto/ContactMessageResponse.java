package com.gestionate.backend.communication.interfaces.rest.dto;

import java.time.LocalDate;

public record ContactMessageResponse(
        Long id,
        String name,
        String email,
        String subject,
        String message,
        LocalDate created_at) {
}
