package com.gestionate.backend.iam.interfaces.rest.dto;

import com.gestionate.backend.iam.domain.model.UserRole;

public record RegisterResponse(
        Long userId,
        Long profileId,
        UserRole role,
        String fullName,
        String email,
        String message) {
}
