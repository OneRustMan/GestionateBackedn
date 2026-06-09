package com.gestionate.backend.shared.interfaces.rest.dto;

public record DistrictResponse(
        Long id,
        String name,
        String province,
        Boolean active) {
}
