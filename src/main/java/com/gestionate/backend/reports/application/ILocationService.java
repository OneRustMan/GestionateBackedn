package com.gestionate.backend.reports.application;

import com.gestionate.backend.reports.domain.model.Location;
import com.gestionate.backend.reports.domain.model.Report;

import java.math.BigDecimal;

public interface ILocationService {

    Location saveReportLocation(
            Report report,
            String districtName,
            String province,
            String addressReference,
            BigDecimal latitude,
            BigDecimal longitude);
}
