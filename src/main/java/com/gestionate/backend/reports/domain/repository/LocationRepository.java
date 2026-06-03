package com.gestionate.backend.reports.domain.repository;

import com.gestionate.backend.reports.domain.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByReport_Id(Long reportId);

    boolean existsByReport_Id(Long reportId);
}
