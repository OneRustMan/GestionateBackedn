package com.gestionate.backend.reception.interfaces.rest.dto;

import com.gestionate.backend.evidences.interfaces.rest.dto.EvidenceResponse;
import com.gestionate.backend.reports.interfaces.rest.dto.IncidentTypeResponse;
import com.gestionate.backend.reports.interfaces.rest.dto.LocationResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ReceptionReportDetailResponse(
        Long reportId,
        String reportCode,
        String description,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        Long citizenId,
        String citizenFullName,
        String citizenDni,
        String citizenPhone,
        String citizenEmail,

        List<IncidentTypeResponse> incidentTypes,
        LocationResponse location,
        List<EvidenceResponse> evidences) {
}
