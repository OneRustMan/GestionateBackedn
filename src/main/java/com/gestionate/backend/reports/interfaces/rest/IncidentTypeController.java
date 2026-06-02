package com.gestionate.backend.reports.interfaces.rest;

import com.gestionate.backend.reports.application.IIncidentTypeService;
import com.gestionate.backend.reports.interfaces.rest.dto.IncidentTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incident-types")
@RequiredArgsConstructor
@Tag(name = "Incident Types", description = "Tipos de incidencia disponibles para reportes ciudadanos")
public class IncidentTypeController {

    private final IIncidentTypeService incidentTypeService;

    @Operation(summary = "Listar tipos de incidencia activos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tipos de incidencia activos")
    })
    @GetMapping
    public ResponseEntity<List<IncidentTypeResponse>> findAllActive() {
        return ResponseEntity.ok(incidentTypeService.findAllActive());
    }
}
