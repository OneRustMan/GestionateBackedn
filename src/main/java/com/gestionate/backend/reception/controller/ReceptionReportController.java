package com.gestionate.backend.reception.controller;

import com.gestionate.backend.reception.dto.ReceptionReportDetailResponse;
import com.gestionate.backend.reception.service.IReceptionReportService;
import com.gestionate.backend.reception.dto.ReceptionReportInboxResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gestionate.backend.reception.dto.DeriveReportRequest;
import com.gestionate.backend.reception.dto.DeriveReportResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/reception/reports")
@RequiredArgsConstructor
@Tag(name = "Reception Reports", description = "Bandeja y revisión de reportes ciudadanos para recepción municipal")
public class ReceptionReportController {

    private final IReceptionReportService receptionReportService;

    @GetMapping("/inbox")
    public ResponseEntity<List<ReceptionReportInboxResponse>> findReportInbox(
            @RequestParam Long receptionistId,

            @RequestParam(required = false) Long incidentTypeId) {
        return ResponseEntity.ok(
                receptionReportService.findReportInbox(receptionistId, incidentTypeId));
    }

    @Operation(summary = "Revisar detalle de un reporte ciudadano")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle del reporte obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "El reporte ya no está disponible")
    })
    @GetMapping("/{reportId}/detail")
    public ResponseEntity<ReceptionReportDetailResponse> findReportDetail(
            @PathVariable Long reportId,
            @RequestParam Long receptionistId) {
        return ResponseEntity.ok(
                receptionReportService.findReportDetail(receptionistId, reportId));
    }

    @Operation(summary = "Derivar reporte ciudadano a limpieza pública")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reporte derivado correctamente y orden de trabajo creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o reporte ya derivado"),
            @ApiResponse(responseCode = "404", description = "Reporte no disponible o recepcionista no encontrado")
    })
    @PostMapping("/{reportId}/derive")
    public ResponseEntity<DeriveReportResponse> deriveReport(
            @PathVariable Long reportId,
            @RequestParam Long receptionistId,
            @Valid @RequestBody DeriveReportRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(receptionReportService.deriveReport(
                        receptionistId,
                        reportId,
                        request));
    }

    @Operation(summary = "Listar reportes derivados o atendidos")
    @GetMapping("/derived")
    public ResponseEntity<List<ReceptionReportInboxResponse>> findDerivedReports(
            @RequestParam Long receptionistId,
            @RequestParam(required = false) Long incidentTypeId) {
        return ResponseEntity.ok(
                receptionReportService.findDerivedReports(receptionistId, incidentTypeId));
    }
}
