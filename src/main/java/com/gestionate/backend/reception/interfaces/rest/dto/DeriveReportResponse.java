package com.gestionate.backend.reception.interfaces.rest.dto;

import com.gestionate.backend.reports.domain.model.ReportStatus;
import com.gestionate.backend.workorders.domain.model.WorkOrderPriority;
import com.gestionate.backend.workorders.domain.model.WorkOrderStatus;

import java.time.LocalDateTime;

public record DeriveReportResponse(
        Long workOrderId,
        String orderCode,
        Long reportId,
        String reportCode,
        Long receptionistId,
        WorkOrderPriority priority,
        WorkOrderStatus workOrderStatus,
        ReportStatus reportStatus,
        LocalDateTime createdAt) {
}
