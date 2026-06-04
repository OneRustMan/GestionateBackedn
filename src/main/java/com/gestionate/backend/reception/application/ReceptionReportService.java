package com.gestionate.backend.reception.application;

import com.gestionate.backend.iam.domain.model.MunicipalReceptionist;
import com.gestionate.backend.iam.domain.repository.MunicipalReceptionistRepository;
import com.gestionate.backend.reception.infrastructure.mapping.ReceptionReportInboxMapper;
import com.gestionate.backend.reception.interfaces.rest.dto.ReceptionReportInboxResponse;
import com.gestionate.backend.reports.domain.model.Location;
import com.gestionate.backend.reports.domain.model.ReportStatus;
import com.gestionate.backend.reports.domain.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceptionReportService implements IReceptionReportService {

    private final MunicipalReceptionistRepository municipalReceptionistRepository;
    private final LocationRepository locationRepository;
    private final ReceptionReportInboxMapper receptionReportInboxMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReceptionReportInboxResponse> findReportInbox(Long receptionistId) {
        MunicipalReceptionist receptionist = municipalReceptionistRepository.findById(receptionistId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Recepcionista municipal no encontrado."));

        Long districtId = receptionist.getMunicipality().getDistrict().getId();

        List<Location> locations = locationRepository
                .findByDistrict_IdAndReport_StatusOrderByReport_CreatedAtDesc(
                        districtId,
                        ReportStatus.RECEIVED);

        return locations.stream()
                .map(location -> receptionReportInboxMapper.toResponse(
                        location.getReport(),
                        location))
                .toList();
    }
}
