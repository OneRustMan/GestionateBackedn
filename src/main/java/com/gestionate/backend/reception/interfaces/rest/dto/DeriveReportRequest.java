package com.gestionate.backend.reception.interfaces.rest.dto;

import com.gestionate.backend.workorders.domain.model.WorkOrderPriority;
import jakarta.validation.constraints.NotNull;

public record DeriveReportRequest(
        @NotNull(message = "Selecciona una prioridad para continuar.") WorkOrderPriority priority) {
}
