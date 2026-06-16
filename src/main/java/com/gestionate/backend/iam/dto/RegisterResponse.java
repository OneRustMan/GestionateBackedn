package com.gestionate.backend.iam.dto;

import com.gestionate.backend.iam.model.UserRole;

public record RegisterResponse(
        Long userId,
        Long profileId,
        UserRole role,
        String fullName,
        String email) {
}
