package com.gestionate.backend.reception.dto;

import com.gestionate.backend.reports.dto.IncidentTypeResponse;
import com.gestionate.backend.reports.dto.LocationResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ReceptionReportInboxResponse(
        Long reportId,
        String reportCode,
        Long citizenId,
        String citizenFullName,
        String description,
        String status,
        List<IncidentTypeResponse> incidentTypes,
        LocationResponse location,
        LocalDateTime createdAt) {
}
