package com.gestionate.backend.reports.service;

import com.gestionate.backend.reports.model.ReportStatus;
import com.gestionate.backend.reports.dto.CreateReportRequest;
import com.gestionate.backend.reports.dto.ReportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IReportService {

    ReportResponse createReport(CreateReportRequest request, List<MultipartFile> files);

    List<ReportResponse> findCitizenReportHistory(
            Long citizenId,
            ReportStatus status,
            Long incidentTypeId);

    ReportResponse findCitizenReportDetail(
            Long citizenId,
            Long reportId);
}
