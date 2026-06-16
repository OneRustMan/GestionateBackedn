package com.gestionate.backend.communication.dto;

import java.time.LocalDate;

public record ContactMessageResponse(
        Long id,
        String name,
        String email,
        String subject,
        String message,
        LocalDate created_at) {
}
