package com.gestionate.backend.iam.repository;

import com.gestionate.backend.iam.model.CleaningOperationsStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CleaningOperationsStaffRepository extends JpaRepository<CleaningOperationsStaff, Long> {

    boolean existsByWorkerCode(String workerCode);

    Optional<CleaningOperationsStaff> findByUser_Id(Long userId);
}
