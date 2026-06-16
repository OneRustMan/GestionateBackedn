package com.gestionate.backend.reports.repository;

import com.gestionate.backend.reports.model.ReportIncidentType;
import com.gestionate.backend.reports.model.ReportIncidentTypeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportIncidentTypeRepository extends JpaRepository<ReportIncidentType, ReportIncidentTypeId> {

    List<ReportIncidentType> findByReport_Id(Long reportId);
}
