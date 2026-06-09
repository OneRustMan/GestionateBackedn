package com.gestionate.backend.reports.interfaces.rest.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record CreateReportRequest(
        @NotNull(message = "El id del ciudadano es obligatorio.") Long citizenId,

        @NotBlank(message = "La descripción del reporte es obligatoria.") @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres.") String description,

        @NotEmpty(message = "Debe seleccionar al menos un tipo de incidencia.") List<@NotNull(message = "El id del tipo de incidencia no puede ser nulo.") Long> incidentTypeIds,

        @NotBlank(message = "El distrito es obligatorio.") @Size(max = 100, message = "El distrito no puede superar los 100 caracteres.") String districtName,

        @NotBlank(message = "La provincia es obligatoria.") @Size(max = 100, message = "La provincia no puede superar los 100 caracteres.") String province,

        @NotBlank(message = "La referencia de dirección es obligatoria.") @Size(max = 255, message = "La referencia de dirección no puede superar los 255 caracteres.") String addressReference,

        @NotNull(message = "La latitud es obligatoria.") @DecimalMin(value = "-90.0", message = "La latitud debe ser mayor o igual a -90.") @DecimalMax(value = "90.0", message = "La latitud debe ser menor o igual a 90.") BigDecimal latitude,

        @NotNull(message = "La longitud es obligatoria.") @DecimalMin(value = "-180.0", message = "La longitud debe ser mayor o igual a -180.") @DecimalMax(value = "180.0", message = "La longitud debe ser menor o igual a 180.") BigDecimal longitude) {
}
