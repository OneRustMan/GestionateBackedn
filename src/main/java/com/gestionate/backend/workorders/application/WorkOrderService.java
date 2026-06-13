package com.gestionate.backend.workorders.application;

import com.gestionate.backend.communication.application.INotificationService;
import com.gestionate.backend.reports.domain.model.Report;
import com.gestionate.backend.workorders.interfaces.rest.dto.CompleteWorkOrderRequest;
import com.gestionate.backend.workorders.interfaces.rest.dto.CompleteWorkOrderResponse;
import com.gestionate.backend.shared.application.util.TextNormalizer;

import java.time.LocalDateTime;
import com.gestionate.backend.evidences.domain.model.Evidence;
import com.gestionate.backend.evidences.domain.repository.EvidenceRepository;
import com.gestionate.backend.iam.domain.model.CleaningOperationsStaff;
import com.gestionate.backend.iam.domain.repository.CleaningOperationsStaffRepository;
import com.gestionate.backend.reports.domain.model.Location;
import com.gestionate.backend.reports.domain.model.ReportStatus;
import com.gestionate.backend.reports.domain.repository.LocationRepository;
import com.gestionate.backend.workorders.domain.model.WorkOrder;
import com.gestionate.backend.workorders.domain.model.WorkOrderPriority;
import com.gestionate.backend.workorders.domain.model.WorkOrderStatus;
import com.gestionate.backend.workorders.domain.repository.WorkOrderRepository;
import com.gestionate.backend.workorders.infrastructure.mapping.WorkOrderMapper;
import com.gestionate.backend.workorders.interfaces.rest.dto.TakeWorkOrderResponse;
import com.gestionate.backend.workorders.interfaces.rest.dto.WorkOrderDetailResponse;
import com.gestionate.backend.workorders.interfaces.rest.dto.WorkOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkOrderService implements IWorkOrderService {

    private final CleaningOperationsStaffRepository cleaningOperationsStaffRepository;
    private final LocationRepository locationRepository;
    private final WorkOrderRepository workOrderRepository;
    private final EvidenceRepository evidenceRepository;
    private final WorkOrderMapper workOrderMapper;
    private final INotificationService notificationService;

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> findAvailableWorkOrders(
            Long cleaningStaffId,
            WorkOrderPriority priority) {
        CleaningOperationsStaff cleaningStaff = cleaningOperationsStaffRepository.findById(cleaningStaffId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Personal operativo no encontrado."));

        Long districtId = cleaningStaff.getMunicipality().getDistrict().getId();

        List<Location> locations = locationRepository
                .findByDistrict_IdAndReport_StatusOrderByReport_CreatedAtDesc(
                        districtId,
                        ReportStatus.DERIVED);

        List<WorkOrderResponse> responses = locations.stream()
                .map(location -> {
                    Optional<WorkOrder> workOrderOptional = workOrderRepository.findByReport_Id(
                            location.getReport().getId());

                    if (workOrderOptional.isEmpty()) {
                        return null;
                    }

                    WorkOrder workOrder = workOrderOptional.get();

                    boolean isPendingAvailable = WorkOrderStatus.PENDING.equals(workOrder.getStatus())
                            && workOrder.getCleaningStaffId() == null;

                    boolean isInProgressAssignedToStaff = WorkOrderStatus.IN_PROGRESS.equals(workOrder.getStatus())
                            && cleaningStaff.getId().equals(workOrder.getCleaningStaffId());

                    if (!isPendingAvailable && !isInProgressAssignedToStaff) {
                        return null;
                    }

                    if (priority != null && !priority.equals(workOrder.getPriority())) {
                        return null;
                    }

                    return workOrderMapper.toResponse(workOrder, location);
                })
                .filter(response -> response != null)
                .toList();

        if (priority != null) {
            return responses.stream()
                    .sorted(Comparator.comparing(WorkOrderResponse::createdAt))
                    .toList();
        }

        return responses.stream()
                .sorted(Comparator.comparingInt(response -> switch (response.priority()) {
                    case HIGH -> 1;
                    case MEDIUM -> 2;
                    case LOW -> 3;
                }))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> findCompletedWorkOrders(
            Long cleaningStaffId,
            WorkOrderPriority priority) {
        CleaningOperationsStaff cleaningStaff = cleaningOperationsStaffRepository.findById(cleaningStaffId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Personal operativo no encontrado."));

        Long districtId = cleaningStaff.getMunicipality().getDistrict().getId();

        List<WorkOrderStatus> statuses = List.of(
                WorkOrderStatus.PARTIAL_ATTENTION,
                WorkOrderStatus.COMPLETED);

        List<WorkOrder> workOrders;

        if (priority != null) {
            workOrders = workOrderRepository.findByCleaningStaffIdAndStatusInAndPriorityOrderByCreatedAtDesc(
                    cleaningStaffId,
                    statuses,
                    priority);
        } else {
            workOrders = workOrderRepository.findByCleaningStaffIdAndStatusInOrderByCreatedAtDesc(
                    cleaningStaffId,
                    statuses);
        }

        return workOrders.stream()
                .map(workOrder -> {
                    Location location = locationRepository.findByReport_Id(workOrder.getReport().getId())
                            .orElse(null);

                    if (location == null) {
                        return null;
                    }

                    if (!districtId.equals(location.getDistrict().getId())) {
                        return null;
                    }

                    return workOrderMapper.toResponse(workOrder, location);
                })
                .filter(response -> response != null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderDetailResponse findWorkOrderDetail(
            Long cleaningStaffId,
            Long workOrderId) {
        CleaningOperationsStaff cleaningStaff = cleaningOperationsStaffRepository.findById(cleaningStaffId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Personal operativo no encontrado."));

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "La orden ya no está disponible."));

        Location location = locationRepository.findByReport_Id(workOrder.getReport().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La orden requiere ubicación para ser atendida."));

        Long cleaningStaffDistrictId = cleaningStaff.getMunicipality().getDistrict().getId();
        Long reportDistrictId = location.getDistrict().getId();

        if (!cleaningStaffDistrictId.equals(reportDistrictId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "La orden ya no está disponible.");
        }

        List<Evidence> evidences = evidenceRepository.findByReport_Id(
                workOrder.getReport().getId());

        return workOrderMapper.toDetailResponse(
                workOrder,
                location,
                evidences);
    }

    @Override
    @Transactional
    public TakeWorkOrderResponse takeWorkOrder(
            Long cleaningStaffId,
            Long workOrderId) {
        CleaningOperationsStaff cleaningStaff = cleaningOperationsStaffRepository.findById(cleaningStaffId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Personal operativo no encontrado."));

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "La orden ya no está disponible."));

        Location location = locationRepository.findByReport_Id(workOrder.getReport().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "La orden ya no está disponible."));

        Long cleaningStaffDistrictId = cleaningStaff.getMunicipality().getDistrict().getId();
        Long reportDistrictId = location.getDistrict().getId();

        if (!cleaningStaffDistrictId.equals(reportDistrictId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "La orden ya no está disponible.");
        }

        if (workOrder.getCleaningStaffId() != null
                || !WorkOrderStatus.PENDING.equals(workOrder.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La orden ya fue asignada.");
        }

        workOrder.setCleaningStaffId(cleaningStaff.getId());
        workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);

        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

        return workOrderMapper.toTakeResponse(savedWorkOrder);
    }

    @Override
    @Transactional
    public CompleteWorkOrderResponse completeWorkOrder(
            Long cleaningStaffId,
            Long workOrderId,
            CompleteWorkOrderRequest request) {
        CleaningOperationsStaff cleaningStaff = cleaningOperationsStaffRepository.findById(cleaningStaffId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Personal operativo no encontrado."));

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No puedes completar esta orden."));

        if (request == null || !TextNormalizer.hasText(request.observation())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ingresa una observación para completar la orden.");
        }

        Location location = locationRepository.findByReport_Id(workOrder.getReport().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No puedes completar esta orden."));

        Long cleaningStaffDistrictId = cleaningStaff.getMunicipality().getDistrict().getId();
        Long reportDistrictId = location.getDistrict().getId();

        if (!cleaningStaffDistrictId.equals(reportDistrictId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No puedes completar esta orden.");
        }

        if (workOrder.getCleaningStaffId() == null
                || !workOrder.getCleaningStaffId().equals(cleaningStaff.getId())
                || !WorkOrderStatus.IN_PROGRESS.equals(workOrder.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No puedes completar esta orden.");
        }

        LocalDateTime now = LocalDateTime.now();

        workOrder.setStatus(WorkOrderStatus.COMPLETED);
        workOrder.setObservation(TextNormalizer.normalizeText(request.observation()));
        workOrder.setCompletedAt(now);

        Report report = workOrder.getReport();
        report.setStatus(ReportStatus.ORDER_COMPLETED);
        report.setUpdatedAt(now);

        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

        notificationService.notifyReportStatusChanged(
                report.getCitizen().getUser(),
                report.getReportCode(),
                report.getStatus());

        return workOrderMapper.toCompleteResponse(savedWorkOrder);
    }
}
