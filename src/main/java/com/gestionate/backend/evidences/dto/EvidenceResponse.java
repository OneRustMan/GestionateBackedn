package com.gestionate.backend.evidences.dto;

import java.time.LocalDateTime;

public record EvidenceResponse(
        Long id,
        Long reportId,
        String fileUrl,
        String fileType,
        LocalDateTime uploadedAt) {
}
