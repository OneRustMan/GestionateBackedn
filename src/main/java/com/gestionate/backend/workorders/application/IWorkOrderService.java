package com.gestionate.backend.workorders.application;

import com.gestionate.backend.workorders.interfaces.rest.dto.WorkOrderResponse;

import java.util.List;

public interface IWorkOrderService {

    List<WorkOrderResponse> findAvailableWorkOrders(Long cleaningStaffId);
}
