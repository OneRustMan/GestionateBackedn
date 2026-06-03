package com.gestionate.backend.reports.interfaces.rest.dto;

import java.math.BigDecimal;

public record LocationResponse(
        Long id,
        Long reportId,
        Long districtId,
        String districtName,
        String province,
        String addressReference,
        BigDecimal latitude,
        BigDecimal longitude) {
}
