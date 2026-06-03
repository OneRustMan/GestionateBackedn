package com.gestionate.backend.reports.interfaces.rest;

import com.gestionate.backend.reports.application.IReportService;
import com.gestionate.backend.reports.interfaces.rest.dto.CreateReportRequest;
import com.gestionate.backend.reports.interfaces.rest.dto.ReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
@Tag(name = "Reports", description = "Gestión de reportes ciudadanos")
public class ReportController {

    private final IReportService reportService;

    @Operation(summary = "Crear reporte ciudadano con tipos de incidencia y evidencias fotográficas")
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

            @RequestPart("files") @NotEmpty(message = "Debe adjuntar al menos una evidencia fotográfica.") @Size(max = 3, message = "Solo puede adjuntar como máximo 3 evidencias fotográficas.") List<MultipartFile> files) {
        CreateReportRequest request = new CreateReportRequest(
                citizenId,
                description,
                incidentTypeIds);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reportService.createReport(request, files));
    }
}
