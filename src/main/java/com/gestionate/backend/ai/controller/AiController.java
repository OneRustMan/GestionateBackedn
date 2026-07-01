package com.gestionate.backend.ai.controller;

import com.gestionate.backend.ai.dto.AiChatRequest;
import com.gestionate.backend.ai.dto.CitizenReportAiRequest;
import com.gestionate.backend.ai.dto.CitizenReportAiResponse;
import com.gestionate.backend.ai.dto.CitizenSupportAiResponse;
import com.gestionate.backend.ai.dto.RagIngestResponse;
import com.gestionate.backend.ai.service.IAiService;
import com.gestionate.backend.ai.service.IRagService;
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
    private final IRagService ragService;

    @Operation(summary = "Asistencia IA para mejorar un reporte ciudadano", description = "Analiza la descripción, tipos de incidencia, ubicación y existencia de evidencia para sugerir mejoras al reporte ciudadano.")
    @PostMapping("/citizen/report-assist")
    public ResponseEntity<CitizenReportAiResponse> assistCitizenReport(
            @RequestBody CitizenReportAiRequest request) {

        CitizenReportAiResponse response = aiService.assistCitizenReport(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Ingestar documentos RAG", description = "Lee los documentos PDF internos de Gestionate, los divide en fragmentos y los guarda en pgvector.")
    @PostMapping("/knowledge/ingest")
    public ResponseEntity<RagIngestResponse> ingestDocuments() {
        RagIngestResponse response = ragService.ingestDocuments();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Asistente IA de ayuda para el ciudadano", description = "Responde preguntas sobre Gestionate usando documentos internos del sistema mediante RAG.")
    @PostMapping("/support/ask")
    public ResponseEntity<CitizenSupportAiResponse> askCitizenSupport(
            @RequestBody AiChatRequest request) {

        CitizenSupportAiResponse response = ragService.askCitizenSupport(request);
        return ResponseEntity.ok(response);
    }
}
