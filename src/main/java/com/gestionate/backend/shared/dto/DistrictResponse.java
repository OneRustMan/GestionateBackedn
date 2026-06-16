package com.gestionate.backend.shared.dto;

public record DistrictResponse(
        Long id,
        String name,
        String province,
        Boolean active) {
}
