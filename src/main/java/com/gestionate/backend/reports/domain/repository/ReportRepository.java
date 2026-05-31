package com.gestionate.backend.reports.domain.repository;

import com.gestionate.backend.reports.domain.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findTopByOrderByIdDesc();

    List<Report> findByCitizen_Id(Long citizenId);
}
