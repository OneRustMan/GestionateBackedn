package com.gestionate.backend.reception.interfaces.rest.dto;

import com.gestionate.backend.reports.interfaces.rest.dto.IncidentTypeResponse;
import com.gestionate.backend.reports.interfaces.rest.dto.LocationResponse;

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
