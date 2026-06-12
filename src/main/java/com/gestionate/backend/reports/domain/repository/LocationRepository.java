package com.gestionate.backend.reports.domain.repository;

import com.gestionate.backend.reports.domain.model.Location;
import com.gestionate.backend.reports.domain.model.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByReport_Id(Long reportId);

    boolean existsByReport_Id(Long reportId);

    List<Location> findByDistrict_IdAndReport_StatusOrderByReport_CreatedAtDesc(
            Long districtId,
            ReportStatus status);

    List<Location> findByDistrict_IdAndReport_StatusAndReport_ReportIncidentTypes_IncidentType_IdOrderByReport_CreatedAtDesc(
            Long districtId,
            ReportStatus status,
            Long incidentTypeId);

    List<Location> findByDistrict_IdAndReport_StatusInOrderByReport_CreatedAtDesc(
            Long districtId,
            List<ReportStatus> statuses);
}
