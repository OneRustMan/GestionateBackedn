package com.gestionate.backend.workorders.dto;

import com.gestionate.backend.workorders.model.WorkOrderStatus;

public record TakeWorkOrderResponse(
        Long workOrderId,
        String orderCode,
        Long reportId,
        String reportCode,
        Long cleaningStaffId,
        WorkOrderStatus workOrderStatus) {
}
