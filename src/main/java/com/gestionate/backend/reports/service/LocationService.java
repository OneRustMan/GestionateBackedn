package com.gestionate.backend.reports.service;

import com.gestionate.backend.reports.model.Location;
import com.gestionate.backend.reports.model.Report;
import com.gestionate.backend.reports.repository.LocationRepository;
import com.gestionate.backend.shared.service.DistrictService;
import com.gestionate.backend.shared.util.TextNormalizer;
import com.gestionate.backend.shared.model.District;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LocationService implements ILocationService {

    private static final BigDecimal MIN_LATITUDE = BigDecimal.valueOf(-90);
    private static final BigDecimal MAX_LATITUDE = BigDecimal.valueOf(90);
    private static final BigDecimal MIN_LONGITUDE = BigDecimal.valueOf(-180);
    private static final BigDecimal MAX_LONGITUDE = BigDecimal.valueOf(180);

    private final LocationRepository locationRepository;
    private final DistrictService districtService;

    @Override
    @Transactional
    public Location saveReportLocation(
            Report report,
            String districtName,
            String province,
            String addressReference,
            BigDecimal latitude,
            BigDecimal longitude) {
        validateReport(report);
        validateLocationData(districtName, province, addressReference, latitude, longitude);

        if (locationRepository.existsByReport_Id(report.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El reporte ya tiene una ubicación registrada.");
        }

        District district = districtService.resolveDistrict(null, districtName, province);

        Location location = Location.builder()
                .report(report)
                .district(district)
                .addressReference(TextNormalizer.normalizeText(addressReference))
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return locationRepository.save(location);
    }

    private void validateReport(Report report) {
        if (report == null || report.getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El reporte es obligatorio para registrar la ubicación.");
        }
    }

    private void validateLocationData(
            String districtName,
            String province,
            String addressReference,
            BigDecimal latitude,
            BigDecimal longitude) {
        if (!TextNormalizer.hasText(districtName)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El distrito es obligatorio.");
        }

        if (!TextNormalizer.hasText(province)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La provincia es obligatoria.");
        }

        if (!TextNormalizer.hasText(addressReference)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La referencia de dirección es obligatoria.");
        }

        validateLatitude(latitude);
        validateLongitude(longitude);
    }

    private void validateLatitude(BigDecimal latitude) {
        if (latitude == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La latitud es obligatoria.");
        }

        if (latitude.compareTo(MIN_LATITUDE) < 0 || latitude.compareTo(MAX_LATITUDE) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La latitud debe estar entre -90 y 90.");
        }
    }

    private void validateLongitude(BigDecimal longitude) {
        if (longitude == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La longitud es obligatoria.");
        }

        if (longitude.compareTo(MIN_LONGITUDE) < 0 || longitude.compareTo(MAX_LONGITUDE) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La longitud debe estar entre -180 y 180.");
        }
    }
}
