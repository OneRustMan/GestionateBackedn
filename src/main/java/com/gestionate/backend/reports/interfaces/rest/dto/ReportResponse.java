package com.gestionate.backend.reports.interfaces.rest.dto;

import java.time.LocalDateTime;

public record ReportResponse(
        Long id,
        String reportCode,
        Long citizenId,
        String description,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
