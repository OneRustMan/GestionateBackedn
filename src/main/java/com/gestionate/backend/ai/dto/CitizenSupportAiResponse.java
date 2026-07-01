package com.gestionate.backend.ai.dto;

public record CitizenSupportAiResponse(
        String reply,
        Boolean answeredFromDocs) {
}
