package com.gestionate.backend.reception.application;

import com.gestionate.backend.evidences.domain.model.Evidence;
import com.gestionate.backend.evidences.domain.repository.EvidenceRepository;
import com.gestionate.backend.iam.domain.model.MunicipalReceptionist;
import com.gestionate.backend.iam.domain.repository.MunicipalReceptionistRepository;
import com.gestionate.backend.reception.infrastructure.mapping.ReceptionReportDetailMapper;
import com.gestionate.backend.reception.infrastructure.mapping.ReceptionReportInboxMapper;
import com.gestionate.backend.reception.interfaces.rest.dto.ReceptionReportDetailResponse;
import com.gestionate.backend.reception.interfaces.rest.dto.ReceptionReportInboxResponse;
import com.gestionate.backend.reports.domain.model.Location;
import com.gestionate.backend.reports.domain.model.Report;
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
    private final EvidenceRepository evidenceRepository;
    private final ReceptionReportInboxMapper receptionReportInboxMapper;
    private final ReceptionReportDetailMapper receptionReportDetailMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReceptionReportInboxResponse> findReportInbox(
            Long receptionistId,
            Long incidentTypeId) {
        MunicipalReceptionist receptionist = municipalReceptionistRepository.findById(receptionistId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Recepcionista municipal no encontrado."));

        Long districtId = receptionist.getMunicipality().getDistrict().getId();

        List<Location> locations;

        if (incidentTypeId != null) {
            locations = locationRepository
                    .findByDistrict_IdAndReport_StatusAndReport_ReportIncidentTypes_IncidentType_IdOrderByReport_CreatedAtDesc(
                            districtId,
                            ReportStatus.RECEIVED,
                            incidentTypeId);
        } else {
            locations = locationRepository
                    .findByDistrict_IdAndReport_StatusOrderByReport_CreatedAtDesc(
                            districtId,
                            ReportStatus.RECEIVED);
        }

        return locations.stream()
                .map(location -> receptionReportInboxMapper.toResponse(
                        location.getReport(),
                        location))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReceptionReportDetailResponse findReportDetail(Long receptionistId, Long reportId) {
        MunicipalReceptionist receptionist = municipalReceptionistRepository.findById(receptionistId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Recepcionista municipal no encontrado."));

        Location location = locationRepository.findByReport_Id(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El reporte ya no está disponible."));

        Long receptionistDistrictId = receptionist.getMunicipality().getDistrict().getId();
        Long reportDistrictId = location.getDistrict().getId();

        if (!receptionistDistrictId.equals(reportDistrictId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El reporte ya no está disponible.");
        }

        Report report = location.getReport();

        if (!ReportStatus.RECEIVED.equals(report.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El reporte ya no está disponible.");
        }

        List<Evidence> evidences = evidenceRepository.findByReport_Id(report.getId());

        return receptionReportDetailMapper.toResponse(
                report,
                location,
                evidences);
    }

}
