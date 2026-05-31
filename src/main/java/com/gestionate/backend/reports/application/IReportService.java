package com.gestionate.backend.reports.application;

import com.gestionate.backend.reports.interfaces.rest.dto.CreateReportRequest;
import com.gestionate.backend.reports.interfaces.rest.dto.ReportResponse;

public interface IReportService {

    ReportResponse createReport(CreateReportRequest request);
}
