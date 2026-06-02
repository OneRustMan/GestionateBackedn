package com.gestionate.backend.evidences.domain.repository;

import com.gestionate.backend.evidences.domain.model.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {

    List<Evidence> findByReport_Id(Long reportId);
}
