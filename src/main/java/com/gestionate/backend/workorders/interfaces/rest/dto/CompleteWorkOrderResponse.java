package com.gestionate.backend.workorders.interfaces.rest.dto;

import com.gestionate.backend.reports.domain.model.ReportStatus;
import com.gestionate.backend.workorders.domain.model.WorkOrderStatus;

import java.time.LocalDateTime;

public record CompleteWorkOrderResponse(
        Long workOrderId,
        String orderCode,
        Long reportId,
        String reportCode,
        Long cleaningStaffId,
        WorkOrderStatus workOrderStatus,
        ReportStatus reportStatus,
        String observation,
        LocalDateTime completedAt) {
}
