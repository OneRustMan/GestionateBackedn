package com.gestionate.backend.workorders.service;

import com.gestionate.backend.workorders.dto.CompleteWorkOrderRequest;
import com.gestionate.backend.workorders.dto.CompleteWorkOrderResponse;
import com.gestionate.backend.workorders.model.WorkOrderPriority;
import com.gestionate.backend.workorders.dto.TakeWorkOrderResponse;
import com.gestionate.backend.workorders.dto.WorkOrderDetailResponse;
import com.gestionate.backend.workorders.dto.WorkOrderResponse;

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

    CompleteWorkOrderResponse completeWorkOrder(
            Long cleaningStaffId,
            Long workOrderId,
            CompleteWorkOrderRequest request);

    List<WorkOrderResponse> findCompletedWorkOrders(
            Long cleaningStaffId,
            WorkOrderPriority priority);
}
