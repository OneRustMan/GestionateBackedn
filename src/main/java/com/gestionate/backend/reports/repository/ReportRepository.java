package com.gestionate.backend.reports.repository;

import com.gestionate.backend.reports.model.Report;
import com.gestionate.backend.reports.model.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findTopByOrderByIdDesc();

    List<Report> findByCitizen_IdOrderByCreatedAtDesc(Long citizenId);

    List<Report> findByCitizen_IdAndStatusOrderByCreatedAtDesc(
            Long citizenId,
            ReportStatus status);

    List<Report> findDistinctByCitizen_IdAndReportIncidentTypes_IncidentType_IdOrderByCreatedAtDesc(
            Long citizenId,
            Long incidentTypeId);

    Optional<Report> findByIdAndCitizen_Id(Long reportId, Long citizenId);

    List<Report> findByCitizen_District_IdAndStatusInOrderByCreatedAtDesc(
            Long districtId,
            List<ReportStatus> statuses);
}
