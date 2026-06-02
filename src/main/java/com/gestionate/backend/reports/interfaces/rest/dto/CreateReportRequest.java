package com.gestionate.backend.reports.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateReportRequest(

        @NotNull(message = "El id del ciudadano es obligatorio.") Long citizenId,

        @NotBlank(message = "La descripción del reporte es obligatoria.") @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres.") String description,

        @NotEmpty(message = "Debe seleccionar al menos un tipo de incidencia.") List<@NotNull(message = "El id del tipo de incidencia no puede ser nulo.") Long> incidentTypeIds) {
}
