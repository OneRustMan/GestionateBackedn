package com.gestionate.backend.evidences.repository;

import com.gestionate.backend.evidences.model.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {

    List<Evidence> findByReport_Id(Long reportId);
}
