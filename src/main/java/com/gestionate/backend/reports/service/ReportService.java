package com.gestionate.backend.reports.service;

import com.gestionate.backend.communication.service.INotificationService;
import com.gestionate.backend.evidences.service.EvidenceService;
import com.gestionate.backend.evidences.model.Evidence;
import com.gestionate.backend.evidences.repository.EvidenceRepository;
import com.gestionate.backend.iam.model.Citizen;
import com.gestionate.backend.iam.repository.CitizenRepository;
import com.gestionate.backend.reports.model.IncidentType;
import com.gestionate.backend.reports.model.Location;
import com.gestionate.backend.reports.model.Report;
import com.gestionate.backend.reports.model.ReportIncidentType;
import com.gestionate.backend.reports.model.ReportStatus;
import com.gestionate.backend.reports.repository.LocationRepository;
import com.gestionate.backend.reports.repository.ReportIncidentTypeRepository;
import com.gestionate.backend.reports.repository.ReportRepository;
import com.gestionate.backend.reports.mapper.ReportMapper;
import com.gestionate.backend.reports.dto.CreateReportRequest;
import com.gestionate.backend.reports.dto.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final INotificationService notificationService;
    private final ReportRepository reportRepository;
    private final CitizenRepository citizenRepository;
    private final IncidentTypeService incidentTypeService;
    private final ReportIncidentTypeRepository reportIncidentTypeRepository;
    private final LocationService locationService;
    private final LocationRepository locationRepository;
    private final EvidenceService evidenceService;
    private final EvidenceRepository evidenceRepository;
    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public ReportResponse createReport(CreateReportRequest request, List<MultipartFile> files) {
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

        Location savedLocation = locationService.saveReportLocation(
                savedReport,
                request.districtName(),
                request.province(),
                request.addressReference(),
                request.latitude(),
                request.longitude());

        List<Evidence> savedEvidences = evidenceService.saveReportEvidences(savedReport, files);

        notificationService.notifyReportStatusChanged(
                savedReport.getCitizen().getUser(),
                savedReport.getReportCode(),
                savedReport.getStatus());

        return reportMapper.toResponse(savedReport, savedEvidences, savedLocation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponse> findCitizenReportHistory(
            Long citizenId,
            ReportStatus status,
            Long incidentTypeId) {
        if (!citizenRepository.existsById(citizenId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Ciudadano no encontrado.");
        }

        if (status != null && incidentTypeId != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo puede aplicar un filtro a la vez.");
        }

        List<Report> reports;

        if (status != null) {
            reports = reportRepository.findByCitizen_IdAndStatusOrderByCreatedAtDesc(
                    citizenId,
                    status);
        } else if (incidentTypeId != null) {
            reports = reportRepository
                    .findDistinctByCitizen_IdAndReportIncidentTypes_IncidentType_IdOrderByCreatedAtDesc(
                            citizenId,
                            incidentTypeId);
        } else {
            reports = reportRepository.findByCitizen_IdOrderByCreatedAtDesc(citizenId);
        }

        return reports.stream()
                .map(report -> {
                    List<Evidence> evidences = evidenceRepository.findByReport_Id(report.getId());

                    Location location = locationRepository.findByReport_Id(report.getId())
                            .orElse(null);

                    return reportMapper.toResponse(report, evidences, location);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponse findCitizenReportDetail(Long citizenId, Long reportId) {
        Report report = reportRepository.findByIdAndCitizen_Id(reportId, citizenId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reporte no encontrado para el ciudadano indicado."));

        List<Evidence> evidences = evidenceRepository.findByReport_Id(report.getId());

        Location location = locationRepository.findByReport_Id(report.getId())
                .orElse(null);

        return reportMapper.toResponse(report, evidences, location);
    }

    private String generateReportCode() {
        Long nextNumber = reportRepository.findTopByOrderByIdDesc()
                .map(report -> report.getId() + 1)
                .orElse(1L);

        return String.format("REPT-%05d", nextNumber);
    }
}
