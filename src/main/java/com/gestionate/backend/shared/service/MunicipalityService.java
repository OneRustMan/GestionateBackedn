package com.gestionate.backend.shared.service;

import com.gestionate.backend.shared.mapper.MunicipalityMapper;
import com.gestionate.backend.shared.dto.MunicipalityResponse;

import java.util.Comparator;
import java.util.List;
import com.gestionate.backend.shared.util.TextNormalizer;
import com.gestionate.backend.shared.model.District;
import com.gestionate.backend.shared.model.Municipality;
import com.gestionate.backend.shared.repository.MunicipalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MunicipalityService implements IMunicipalityService {

    private final MunicipalityMapper municipalityMapper;
    private final MunicipalityRepository municipalityRepository;
    private final DistrictService districtService;

    @Override
    @Transactional
    public Municipality resolveMunicipality(
            Long municipalityId,
            String municipalityName,
            Long districtId,
            String districtName,
            String province) {
        boolean hasMunicipalityId = municipalityId != null;
        boolean hasMunicipalityText = TextNormalizer.hasText(municipalityName)
                || districtId != null
                || TextNormalizer.hasText(districtName)
                || TextNormalizer.hasText(province);

        if (hasMunicipalityId && hasMunicipalityText) {
            throw new IllegalArgumentException(
                    "Debe enviar municipalityId o municipalityName/district, pero no ambos.");
        }

        if (hasMunicipalityId) {
            Municipality municipality = municipalityRepository.findById(municipalityId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "La municipalidad seleccionada no existe."));

            if (!Boolean.TRUE.equals(municipality.getActive())) {
                throw new IllegalArgumentException(
                        "La municipalidad seleccionada no está activa.");
            }

            return municipality;
        }

        if (!TextNormalizer.hasText(municipalityName)) {
            throw new IllegalArgumentException(
                    "Debe enviar el nombre de la municipalidad.");
        }

        District district = districtService.resolveDistrict(
                districtId,
                districtName,
                province);

        String normalizedMunicipalityName = TextNormalizer.normalizeUpperText(municipalityName);

        return municipalityRepository
                .findByNameAndDistrict_Id(normalizedMunicipalityName, district.getId())
                .orElseGet(() -> createMunicipality(normalizedMunicipalityName, district));
    }

    @Override
    @Transactional
    public Municipality createMunicipality(String name, District district) {
        Municipality municipality = Municipality.builder()
                .name(name)
                .district(district)
                .active(true)
                .build();

        return municipalityRepository.save(municipality);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MunicipalityResponse> findActiveMunicipalities() {
        return municipalityRepository.findByActiveTrue()
                .stream()
                .sorted(Comparator.comparing(Municipality::getName))
                .map(municipalityMapper::toResponse)
                .toList();
    }
}
