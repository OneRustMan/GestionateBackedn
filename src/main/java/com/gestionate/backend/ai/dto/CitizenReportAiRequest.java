package com.gestionate.backend.ai.dto;

import java.util.List;

public record CitizenReportAiRequest(
        String description,
        List<String> selectedIncidentTypes,
        String address,
        Double latitude,
        Double longitude,
        Boolean hasEvidence,
        Integer evidenceCount) {
}
