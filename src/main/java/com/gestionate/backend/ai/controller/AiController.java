package com.gestionate.backend.ai.controller;

import com.gestionate.backend.ai.dto.CitizenReportAiRequest;
import com.gestionate.backend.ai.dto.CitizenReportAiResponse;
import com.gestionate.backend.ai.service.IAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "Endpoints de asistencia con inteligencia artificial")
public class AiController {

    private final IAiService aiService;

    @Operation(summary = "Asistencia IA para mejorar un reporte ciudadano", description = "Analiza la descripción, tipos de incidencia, ubicación y existencia de evidencia para sugerir mejoras al reporte ciudadano. La IA no asigna prioridad, no cambia estados y no analiza visualmente imágenes.")
    @PostMapping("/citizen/report-assist")
    public ResponseEntity<CitizenReportAiResponse> assistCitizenReport(
            @RequestBody CitizenReportAiRequest request) {

        CitizenReportAiResponse response = aiService.assistCitizenReport(request);
        return ResponseEntity.ok(response);
    }
}
