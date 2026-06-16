package com.gestionate.backend.reception.dto;

import com.gestionate.backend.reports.model.ReportStatus;
import com.gestionate.backend.workorders.model.WorkOrderPriority;
import com.gestionate.backend.workorders.model.WorkOrderStatus;

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
