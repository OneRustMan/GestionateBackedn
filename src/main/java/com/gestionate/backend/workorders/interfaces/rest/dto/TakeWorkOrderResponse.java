package com.gestionate.backend.workorders.interfaces.rest.dto;

import com.gestionate.backend.workorders.domain.model.WorkOrderStatus;

public record TakeWorkOrderResponse(
        Long workOrderId,
        String orderCode,
        Long reportId,
        String reportCode,
        Long cleaningStaffId,
        WorkOrderStatus workOrderStatus) {
}
