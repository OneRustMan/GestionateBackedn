package com.gestionate.backend.iam.repository;

import com.gestionate.backend.iam.model.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {

    Optional<PasswordResetCode> findTopByUser_EmailAndCodeAndUsedFalseOrderByCreatedAtDesc(
            String email,
            String code);

    List<PasswordResetCode> findByUser_IdAndUsedFalse(Long userId);
}
