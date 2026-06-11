package com.gestionate.backend.shared.interfaces.rest.dto;

public record MunicipalityResponse(
        Long id,
        String name,
        Boolean active,
        Long districtId,
        String districtName,
        String province) {
}
