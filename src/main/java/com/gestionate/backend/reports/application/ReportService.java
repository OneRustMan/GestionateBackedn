package com.gestionate.backend.reports.application;

import com.gestionate.backend.iam.domain.model.Citizen;
import com.gestionate.backend.iam.domain.repository.CitizenRepository;
import com.gestionate.backend.reports.domain.model.IncidentType;
import com.gestionate.backend.reports.domain.model.Report;
import com.gestionate.backend.reports.domain.model.ReportIncidentType;
import com.gestionate.backend.reports.domain.model.ReportStatus;
import com.gestionate.backend.reports.domain.repository.ReportIncidentTypeRepository;
import com.gestionate.backend.reports.domain.repository.ReportRepository;
import com.gestionate.backend.reports.infrastructure.mapping.ReportMapper;
import com.gestionate.backend.reports.interfaces.rest.dto.CreateReportRequest;
import com.gestionate.backend.reports.interfaces.rest.dto.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final ReportRepository reportRepository;
    private final CitizenRepository citizenRepository;
    private final IncidentTypeService incidentTypeService;
    private final ReportIncidentTypeRepository reportIncidentTypeRepository;
    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public ReportResponse createReport(CreateReportRequest request) {
        Citizen citizen = citizenRepository.findById(request.citizenId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ciudadano no encontrado."));

        List<IncidentType> incidentTypes = incidentTypeService
                .findActiveIncidentTypesByIds(request.incidentTypeIds());

        LocalDateTime now = LocalDateTime.now();

        Report report = Report.builder()
                .reportCode(generateReportCode())
                .citizen(citizen)
                .description(request.description().trim())
                .status(ReportStatus.RECEIVED)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Report savedReport = reportRepository.save(report);

        Set<ReportIncidentType> reportIncidentTypes = new LinkedHashSet<>();

        for (IncidentType incidentType : incidentTypes) {
            ReportIncidentType reportIncidentType = new ReportIncidentType(savedReport, incidentType);
            reportIncidentTypes.add(reportIncidentType);
        }

        List<ReportIncidentType> savedReportIncidentTypes = reportIncidentTypeRepository.saveAll(reportIncidentTypes);

        savedReport.setReportIncidentTypes(new LinkedHashSet<>(savedReportIncidentTypes));

        return reportMapper.toResponse(savedReport);
    }

    private String generateReportCode() {
        Long nextNumber = reportRepository.findTopByOrderByIdDesc()
                .map(report -> report.getId() + 1)
                .orElse(1L);

        return String.format("REPT-%05d", nextNumber);
    }
}
