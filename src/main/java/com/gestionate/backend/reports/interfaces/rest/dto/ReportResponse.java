package com.gestionate.backend.reports.interfaces.rest.dto;

import com.gestionate.backend.evidences.interfaces.rest.dto.EvidenceResponse;

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
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
