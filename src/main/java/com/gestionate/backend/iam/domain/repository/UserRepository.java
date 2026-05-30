package com.gestionate.backend.iam.domain.repository;

import com.gestionate.backend.iam.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);
}
