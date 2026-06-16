package com.gestionate.backend.reports.dto;

public record IncidentTypeResponse(
        Long id,
        String name,
        String displayName,
        Boolean active) {
}
