package com.gestionate.backend.workorders.application;

import com.gestionate.backend.iam.domain.model.CleaningOperationsStaff;
import com.gestionate.backend.iam.domain.repository.CleaningOperationsStaffRepository;
import com.gestionate.backend.reports.domain.model.Location;
import com.gestionate.backend.reports.domain.model.ReportStatus;
import com.gestionate.backend.reports.domain.repository.LocationRepository;
import com.gestionate.backend.workorders.domain.model.WorkOrder;
import com.gestionate.backend.workorders.domain.model.WorkOrderStatus;
import com.gestionate.backend.workorders.domain.repository.WorkOrderRepository;
import com.gestionate.backend.workorders.infrastructure.mapping.WorkOrderMapper;
import com.gestionate.backend.workorders.interfaces.rest.dto.WorkOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    public List<WorkOrderResponse> findAvailableWorkOrders(Long cleaningStaffId) {
        CleaningOperationsStaff cleaningStaff = cleaningOperationsStaffRepository.findById(cleaningStaffId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Personal operativo no encontrado."));

        Long districtId = cleaningStaff.getMunicipality().getDistrict().getId();

        List<Location> locations = locationRepository
                .findByDistrict_IdAndReport_StatusOrderByReport_CreatedAtDesc(
                        districtId,
                        ReportStatus.DERIVED);

        return locations.stream()
                .map(location -> {
                    Optional<WorkOrder> workOrder = workOrderRepository.findByReport_Id(
                            location.getReport().getId());

                    if (workOrder.isEmpty()) {
                        return null;
                    }

                    if (!WorkOrderStatus.PENDING.equals(workOrder.get().getStatus())) {
                        return null;
                    }

                    return workOrderMapper.toResponse(workOrder.get(), location);
                })
                .filter(response -> response != null)
                .toList();
    }
}
