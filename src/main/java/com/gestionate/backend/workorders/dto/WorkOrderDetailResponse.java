package com.gestionate.backend.workorders.dto;

import com.gestionate.backend.evidences.dto.EvidenceResponse;
import com.gestionate.backend.reports.model.ReportStatus;
import com.gestionate.backend.reports.dto.IncidentTypeResponse;
import com.gestionate.backend.reports.dto.LocationResponse;
import com.gestionate.backend.workorders.model.WorkOrderPriority;
import com.gestionate.backend.workorders.model.WorkOrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record WorkOrderDetailResponse(
        Long workOrderId,
        String orderCode,

        Long reportId,
        String reportCode,
        String description,
        ReportStatus reportStatus,

        WorkOrderPriority priority,
        WorkOrderStatus workOrderStatus,

        List<IncidentTypeResponse> incidentTypes,
        LocationResponse location,
        List<EvidenceResponse> evidences,

        LocalDateTime createdAt,
        LocalDateTime completedAt) {
}
