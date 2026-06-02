package com.gestionate.backend.reports.interfaces.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ReportResponse(
        Long id,
        String reportCode,
        Long citizenId,
        String description,
        String status,
        List<IncidentTypeResponse> incidentTypes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
