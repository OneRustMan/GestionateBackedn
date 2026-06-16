package com.gestionate.backend.reports.service;

import com.gestionate.backend.reports.model.Location;
import com.gestionate.backend.reports.model.Report;

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
