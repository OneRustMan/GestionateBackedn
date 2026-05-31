package com.gestionate.backend.reports.interfaces.rest;

import com.gestionate.backend.reports.application.IReportService;
import com.gestionate.backend.reports.interfaces.rest.dto.CreateReportRequest;
import com.gestionate.backend.reports.interfaces.rest.dto.ReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Gestión de reportes ciudadanos")
public class ReportController {

    private final IReportService reportService;

    @Operation(summary = "Crear reporte ciudadano")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reporte ciudadano registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Ciudadano no encontrado")
    })
    @PostMapping
    public ResponseEntity<ReportResponse> createReport(
            @Valid @RequestBody CreateReportRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reportService.createReport(request));
    }
}
