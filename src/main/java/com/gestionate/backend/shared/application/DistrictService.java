package com.gestionate.backend.shared.application;

import com.gestionate.backend.shared.application.util.TextNormalizer;
import com.gestionate.backend.shared.domain.model.District;
import com.gestionate.backend.shared.domain.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DistrictService implements IDistrictService {

    private final DistrictRepository districtRepository;

    @Override
    @Transactional
    public District resolveDistrict(Long districtId, String districtName, String province) {
        boolean hasDistrictId = districtId != null;
        boolean hasDistrictText = TextNormalizer.hasText(districtName)
                || TextNormalizer.hasText(province);

        if (hasDistrictId && hasDistrictText) {
            throw new IllegalArgumentException(
                    "Debe enviar districtId o districtName/province, pero no ambos.");
        }

        if (hasDistrictId) {
            District district = districtRepository.findById(districtId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El distrito seleccionado no existe."));

            if (!Boolean.TRUE.equals(district.getActive())) {
                throw new IllegalArgumentException(
                        "El distrito seleccionado no está activo.");
            }

            return district;
        }

        if (!TextNormalizer.hasText(districtName) || !TextNormalizer.hasText(province)) {
            throw new IllegalArgumentException(
                    "Debe enviar un distrito existente mediante districtId o escribir districtName y province.");
        }

        String normalizedDistrictName = TextNormalizer.normalizeUpperText(districtName);
        String normalizedProvince = TextNormalizer.normalizeUpperText(province);

        return districtRepository
                .findByNameAndProvince(normalizedDistrictName, normalizedProvince)
                .orElseGet(() -> createDistrict(normalizedDistrictName, normalizedProvince));
    }

    @Override
    @Transactional
    public District createDistrict(String name, String province) {
        if (!TextNormalizer.hasText(name) || !TextNormalizer.hasText(province)) {
            throw new IllegalArgumentException(
                    "El nombre del distrito y la provincia son obligatorios.");
        }

        String normalizedName = TextNormalizer.normalizeUpperText(name);
        String normalizedProvince = TextNormalizer.normalizeUpperText(province);

        if (districtRepository.findByNameAndProvince(normalizedName, normalizedProvince).isPresent()) {
            throw new IllegalArgumentException(
                    "El distrito ya existe en la provincia indicada.");
        }

        District district = District.builder()
                .name(normalizedName)
                .province(normalizedProvince)
                .active(true)
                .build();

        return districtRepository.save(district);
    }
}
