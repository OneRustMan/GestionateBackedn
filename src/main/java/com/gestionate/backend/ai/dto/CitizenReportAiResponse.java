package com.gestionate.backend.ai.dto;

import java.util.List;

public record CitizenReportAiResponse(
        String summary,
        List<String> suggestedIncidentTypes,
        String descriptionImprovement,
        List<String> missingInformation,
        String citizenMessage) {
}
