package com.gestionate.backend.reports.interfaces.rest;

import com.gestionate.backend.reports.domain.model.ReportStatus;
import com.gestionate.backend.reports.application.IReportService;
import com.gestionate.backend.reports.interfaces.rest.dto.CreateReportRequest;
import com.gestionate.backend.reports.interfaces.rest.dto.ReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
@Tag(name = "Reports", description = "Gestión de reportes ciudadanos")
public class ReportController {

    private final IReportService reportService;

    @Operation(summary = "Crear reporte ciudadano con tipos de incidencia, evidencias y ubicación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reporte ciudadano registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Ciudadano no encontrado")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReportResponse> createReport(
            @RequestParam @NotNull(message = "El id del ciudadano es obligatorio.") Long citizenId,

            @RequestParam @NotBlank(message = "La descripción del reporte es obligatoria.") @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres.") String description,

            @RequestParam @NotEmpty(message = "Debe seleccionar al menos un tipo de incidencia.") List<@NotNull(message = "El id del tipo de incidencia no puede ser nulo.") Long> incidentTypeIds,

            @RequestParam @NotBlank(message = "El distrito es obligatorio.") @Size(max = 100, message = "El distrito no puede superar los 100 caracteres.") String districtName,

            @RequestParam @NotBlank(message = "La provincia es obligatoria.") @Size(max = 100, message = "La provincia no puede superar los 100 caracteres.") String province,

            @RequestParam @NotBlank(message = "La referencia de dirección es obligatoria.") @Size(max = 255, message = "La referencia de dirección no puede superar los 255 caracteres.") String addressReference,

            @RequestParam @NotNull(message = "La latitud es obligatoria.") @DecimalMin(value = "-90.0", message = "La latitud debe ser mayor o igual a -90.") @DecimalMax(value = "90.0", message = "La latitud debe ser menor o igual a 90.") BigDecimal latitude,

            @RequestParam @NotNull(message = "La longitud es obligatoria.") @DecimalMin(value = "-180.0", message = "La longitud debe ser mayor o igual a -180.") @DecimalMax(value = "180.0", message = "La longitud debe ser menor o igual a 180.") BigDecimal longitude,

            @RequestPart("files") @NotEmpty(message = "Debe adjuntar al menos una evidencia fotográfica.") @Size(max = 3, message = "Solo puede adjuntar como máximo 3 evidencias fotográficas.") List<MultipartFile> files) {
        CreateReportRequest request = new CreateReportRequest(
                citizenId,
                description,
                incidentTypeIds,
                districtName,
                province,
                addressReference,
                latitude,
                longitude);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reportService.createReport(request, files));
    }

    @Operation(summary = "Ver historial de reportes de un ciudadano")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial de reportes obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "Filtros inválidos"),
            @ApiResponse(responseCode = "404", description = "Ciudadano no encontrado")
    })
    @GetMapping("/citizens/{citizenId}/history")
    public ResponseEntity<List<ReportResponse>> findCitizenReportHistory(
            @PathVariable Long citizenId,

            @RequestParam(required = false) ReportStatus status,

            @RequestParam(required = false) Long incidentTypeId) {
        return ResponseEntity.ok(
                reportService.findCitizenReportHistory(citizenId, status, incidentTypeId));
    }
}
