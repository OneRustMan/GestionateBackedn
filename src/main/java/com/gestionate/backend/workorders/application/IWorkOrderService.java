package com.gestionate.backend.workorders.application;

import com.gestionate.backend.workorders.domain.model.WorkOrderPriority;
import com.gestionate.backend.workorders.interfaces.rest.dto.TakeWorkOrderResponse;
import com.gestionate.backend.workorders.interfaces.rest.dto.WorkOrderDetailResponse;
import com.gestionate.backend.workorders.interfaces.rest.dto.WorkOrderResponse;

import java.util.List;

public interface IWorkOrderService {

    List<WorkOrderResponse> findAvailableWorkOrders(
            Long cleaningStaffId,
            WorkOrderPriority priority);

    WorkOrderDetailResponse findWorkOrderDetail(
            Long cleaningStaffId,
            Long workOrderId);

    TakeWorkOrderResponse takeWorkOrder(
            Long cleaningStaffId,
            Long workOrderId);
}
