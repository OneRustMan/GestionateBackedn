package com.gestionate.backend.reception.interfaces.rest;

import com.gestionate.backend.reception.application.IReceptionReportService;
import com.gestionate.backend.reception.interfaces.rest.dto.ReceptionReportInboxResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reception/reports")
@RequiredArgsConstructor
@Tag(name = "Reception Reports", description = "Bandeja y revisión de reportes ciudadanos para recepción municipal")
public class ReceptionReportController {

    private final IReceptionReportService receptionReportService;

    @Operation(summary = "Ver bandeja de reportes ciudadanos recibidos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bandeja de reportes obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Recepcionista municipal no encontrado")
    })
    @GetMapping("/inbox")
    public ResponseEntity<List<ReceptionReportInboxResponse>> findReportInbox(
            @RequestParam Long receptionistId) {
        return ResponseEntity.ok(
                receptionReportService.findReportInbox(receptionistId));
    }
}
