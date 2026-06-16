package com.gestionate.backend.reports.repository;

import com.gestionate.backend.reports.model.IncidentType;
import com.gestionate.backend.reports.model.IncidentTypeName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncidentTypeRepository extends JpaRepository<IncidentType, Long> {

    List<IncidentType> findByActiveTrue();

    Optional<IncidentType> findByName(IncidentTypeName name);
}
