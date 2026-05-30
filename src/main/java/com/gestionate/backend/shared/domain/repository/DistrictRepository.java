package com.gestionate.backend.shared.domain.repository;

import com.gestionate.backend.shared.domain.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long> {

    List<District> findByActiveTrue();

    Optional<District> findByNameAndProvince(String name, String province);
}
