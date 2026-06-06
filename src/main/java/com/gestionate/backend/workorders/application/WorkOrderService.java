package com.gestionate.backend.workorders.application;

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
    private final WorkOrderMapper workOrderMapper;

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

                    if (!WorkOrderStatus.PENDING.equals(workOrder.getStatus())) {
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
}
