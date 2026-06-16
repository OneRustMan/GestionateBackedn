package com.gestionate.backend.reports.dto;

import com.gestionate.backend.evidences.dto.EvidenceResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ReportResponse(
        Long id,
        String reportCode,
        Long citizenId,
        String description,
        String status,
        List<IncidentTypeResponse> incidentTypes,
        List<EvidenceResponse> evidences,
        LocationResponse location,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
