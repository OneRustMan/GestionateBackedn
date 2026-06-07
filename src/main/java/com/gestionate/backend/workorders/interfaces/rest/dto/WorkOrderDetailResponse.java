package com.gestionate.backend.workorders.interfaces.rest.dto;

import com.gestionate.backend.evidences.interfaces.rest.dto.EvidenceResponse;
import com.gestionate.backend.reports.domain.model.ReportStatus;
import com.gestionate.backend.reports.interfaces.rest.dto.IncidentTypeResponse;
import com.gestionate.backend.reports.interfaces.rest.dto.LocationResponse;
import com.gestionate.backend.workorders.domain.model.WorkOrderPriority;
import com.gestionate.backend.workorders.domain.model.WorkOrderStatus;

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
