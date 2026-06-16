package com.gestionate.backend.shared.repository;

import com.gestionate.backend.shared.model.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {

    List<Municipality> findByActiveTrue();

    Optional<Municipality> findByNameAndDistrict_Id(String name, Long districtId);
}
