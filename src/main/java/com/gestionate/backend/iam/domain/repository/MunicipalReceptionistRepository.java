package com.gestionate.backend.iam.domain.repository;

import com.gestionate.backend.iam.domain.model.MunicipalReceptionist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MunicipalReceptionistRepository extends JpaRepository<MunicipalReceptionist, Long> {

    boolean existsByWorkerCode(String workerCode);

    Optional<MunicipalReceptionist> findByUser_Id(Long userId);
}
