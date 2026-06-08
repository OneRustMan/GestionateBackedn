package com.gestionate.backend.iam.domain.repository;

import java.util.Optional;
import com.gestionate.backend.iam.domain.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Optional<Citizen> findByUser_Id(Long userId);
}
