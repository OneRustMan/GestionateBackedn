package com.gestionate.backend.reports.application;

import com.gestionate.backend.iam.domain.model.Citizen;
import com.gestionate.backend.iam.domain.repository.CitizenRepository;
import com.gestionate.backend.reports.domain.model.Report;
import com.gestionate.backend.reports.domain.model.ReportStatus;
import com.gestionate.backend.reports.domain.repository.ReportRepository;
import com.gestionate.backend.reports.interfaces.rest.dto.CreateReportRequest;
import com.gestionate.backend.reports.interfaces.rest.dto.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final CitizenRepository citizenRepository;

    @Transactional
    public ReportResponse createReport(CreateReportRequest request) {
        Citizen citizen = citizenRepository.findById(request.citizenId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ciudadano no encontrado."));

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

        return toResponse(savedReport, "Reporte registrado correctamente.");
    }

    private String generateReportCode() {
        long nextNumber = reportRepository.findTopByOrderByIdDesc()
                .map(Report::getReportCode)
                .map(this::extractReportNumber)
                .orElse(0L) + 1;

        String reportCode;

        do {
            reportCode = String.format("REPT-%05d", nextNumber);
            nextNumber++;
        } while (reportRepository.existsByReportCode(reportCode));

        return reportCode;
    }

    private long extractReportNumber(String reportCode) {
        if (reportCode == null || !reportCode.startsWith("REPT-")) {
            return 0L;
        }

        try {
            return Long.parseLong(reportCode.substring(5));
        } catch (NumberFormatException exception) {
            return 0L;
        }
    }

    private ReportResponse toResponse(Report report, String message) {
        return new ReportResponse(
                report.getId(),
                report.getReportCode(),
                report.getCitizen().getId(),
                report.getDescription(),
                report.getStatus().name(),
                report.getCreatedAt(),
                report.getUpdatedAt(),
                message);
    }
}
