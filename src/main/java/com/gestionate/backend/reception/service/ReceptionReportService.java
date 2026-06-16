package com.gestionate.backend.reception.service;

import com.gestionate.backend.communication.service.INotificationService;
import com.gestionate.backend.evidences.model.Evidence;
import com.gestionate.backend.evidences.repository.EvidenceRepository;
import com.gestionate.backend.iam.model.MunicipalReceptionist;
import com.gestionate.backend.iam.repository.MunicipalReceptionistRepository;
import com.gestionate.backend.reception.mapper.DeriveReportMapper;
import com.gestionate.backend.reception.mapper.ReceptionReportDetailMapper;
import com.gestionate.backend.reception.mapper.ReceptionReportInboxMapper;
import com.gestionate.backend.reception.dto.DeriveReportRequest;
import com.gestionate.backend.reception.dto.DeriveReportResponse;
import com.gestionate.backend.reception.dto.ReceptionReportDetailResponse;
import com.gestionate.backend.reception.dto.ReceptionReportInboxResponse;
import com.gestionate.backend.reports.model.Location;
import com.gestionate.backend.reports.model.Report;
import com.gestionate.backend.reports.model.ReportStatus;
import com.gestionate.backend.reports.repository.LocationRepository;
import com.gestionate.backend.reports.repository.ReportRepository;
import com.gestionate.backend.workorders.model.WorkOrder;
import com.gestionate.backend.workorders.model.WorkOrderStatus;
import com.gestionate.backend.workorders.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceptionReportService implements IReceptionReportService {

    private final INotificationService notificationService;
    private final MunicipalReceptionistRepository municipalReceptionistRepository;
    private final LocationRepository locationRepository;
    private final EvidenceRepository evidenceRepository;
    private final ReportRepository reportRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ReceptionReportInboxMapper receptionReportInboxMapper;
    private final ReceptionReportDetailMapper receptionReportDetailMapper;
    private final DeriveReportMapper deriveReportMapper;

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

        List<Evidence> evidences = evidenceRepository.findByReport_Id(report.getId());

        return receptionReportDetailMapper.toResponse(
                report,
                location,
                evidences);
    }

    @Override
    @Transactional
    public DeriveReportResponse deriveReport(
            Long receptionistId,
            Long reportId,
            DeriveReportRequest request) {
        if (request.priority() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecciona una prioridad para continuar.");
        }

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

        if (workOrderRepository.existsByReport_Id(report.getId())
                || ReportStatus.DERIVED.equals(report.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El reporte ya fue derivado.");
        }

        if (!ReportStatus.RECEIVED.equals(report.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El reporte ya no está disponible.");
        }

        LocalDateTime now = LocalDateTime.now();

        WorkOrder workOrder = WorkOrder.builder()
                .orderCode(generateWorkOrderCode())
                .report(report)
                .receptionist(receptionist)
                .cleaningStaffId(null)
                .priority(request.priority())
                .status(WorkOrderStatus.PENDING)
                .observation(null)
                .createdAt(now)
                .completedAt(null)
                .build();

        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

        report.setStatus(ReportStatus.DERIVED);
        report.setUpdatedAt(now);
        Report savedReport = reportRepository.save(report);

        notificationService.notifyReportStatusChanged(
                savedReport.getCitizen().getUser(),
                savedReport.getReportCode(),
                savedReport.getStatus());

        return deriveReportMapper.toResponse(savedWorkOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceptionReportInboxResponse> findDerivedReports(
            Long receptionistId,
            Long incidentTypeId) {
        MunicipalReceptionist receptionist = municipalReceptionistRepository.findById(receptionistId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Recepcionista municipal no encontrado."));

        Long districtId = receptionist.getMunicipality().getDistrict().getId();

        List<ReportStatus> statuses = List.of(
                ReportStatus.DERIVED,
                ReportStatus.ORDER_COMPLETED);

        List<Location> locations;

        if (incidentTypeId != null) {
            locations = locationRepository
                    .findByDistrict_IdAndReport_StatusInAndReport_ReportIncidentTypes_IncidentType_IdOrderByReport_CreatedAtDesc(
                            districtId,
                            statuses,
                            incidentTypeId);
        } else {
            locations = locationRepository
                    .findByDistrict_IdAndReport_StatusInOrderByReport_CreatedAtDesc(
                            districtId,
                            statuses);
        }

        return locations.stream()
                .map(location -> receptionReportInboxMapper.toResponse(
                        location.getReport(),
                        location))
                .toList();
    }

    private String generateWorkOrderCode() {
        Long nextNumber = workOrderRepository.findTopByOrderByIdDesc()
                .map(workOrder -> workOrder.getId() + 1)
                .orElse(1L);

        return String.format("ORD-%05d", nextNumber);
    }
}
