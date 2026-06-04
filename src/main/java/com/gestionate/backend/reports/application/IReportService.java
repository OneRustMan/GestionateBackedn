package com.gestionate.backend.reports.application;

import com.gestionate.backend.reports.interfaces.rest.dto.CreateReportRequest;
import com.gestionate.backend.reports.interfaces.rest.dto.ReportResponse;
import org.springframework.web.multipart.MultipartFile;

import com.gestionate.backend.reports.domain.model.ReportStatus;
import java.util.List;

public interface IReportService {

    ReportResponse createReport(CreateReportRequest request, List<MultipartFile> files);

    List<ReportResponse> findCitizenReportHistory(
            Long citizenId,
            ReportStatus status,
            Long incidentTypeId);
}
