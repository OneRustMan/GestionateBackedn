package com.gestionate.backend.reports.interfaces.rest.dto;

public record IncidentTypeResponse(
        Long id,
        String name,
        String displayName,
        Boolean active) {
}
