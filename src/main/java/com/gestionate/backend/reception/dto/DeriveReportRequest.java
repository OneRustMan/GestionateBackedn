package com.gestionate.backend.reception.dto;

import com.gestionate.backend.workorders.model.WorkOrderPriority;
import jakarta.validation.constraints.NotNull;

public record DeriveReportRequest(
        @NotNull(message = "Selecciona una prioridad para continuar.") WorkOrderPriority priority) {
}
