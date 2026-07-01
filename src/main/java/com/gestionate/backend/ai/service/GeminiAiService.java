package com.gestionate.backend.ai.service;

import com.gestionate.backend.ai.dto.CitizenSupportAiResponse;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionate.backend.ai.dto.CitizenReportAiRequest;
import com.gestionate.backend.ai.dto.CitizenReportAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiAiService implements IAiService {

    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";

    private final ObjectMapper objectMapper;

    @Value("${ai.gemini.api-key}")
    private String geminiApiKey;

    @Value("${ai.gemini.model}")
    private String geminiModel;

    @Override
    public CitizenReportAiResponse assistCitizenReport(CitizenReportAiRequest request) {
        validateCitizenReportRequest(request);

        if (!StringUtils.hasText(geminiApiKey)) {
            throw new IllegalStateException("La API key de Gemini no está configurada.");
        }

        try {
            String prompt = buildCitizenReportPrompt(request);

            String body = """
                    {
                      "contents": [
                        {
                          "parts": [
                            {
                              "text": %s
                            }
                          ]
                        }
                      ],
                      "generationConfig": {
                        "temperature": 0.2,
                        "responseMimeType": "application/json"
                      }
                    }
                    """.formatted(objectMapper.writeValueAsString(prompt));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_URL.formatted(geminiModel, geminiApiKey)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.error("Gemini respondió con error. Status: {}. Body: {}", response.statusCode(), response.body());

                throw new IllegalStateException(
                        "No se pudo obtener respuesta de Gemini. Status: "
                                + response.statusCode()
                                + ". Response: "
                                + response.body());
            }

            String aiText = extractGeminiText(response.body());
            String cleanJson = cleanJson(aiText);

            CitizenReportAiResponse aiResponse = objectMapper.readValue(cleanJson, CitizenReportAiResponse.class);

            return new CitizenReportAiResponse(
                    aiResponse.summary(),
                    aiResponse.suggestedIncidentTypes(),
                    aiResponse.descriptionImprovement(),
                    buildObjectiveMissingInformation(request),
                    aiResponse.citizenMessage());

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("La asistencia IA fue interrumpida.", exception);
        } catch (Exception exception) {
            log.error("Error completo al generar asistencia IA del reporte. Request: {}", request, exception);
            throw new IllegalStateException(
                    "No se pudo generar la asistencia IA del reporte. Causa: " + exception.getMessage(),
                    exception);
        }
    }

    public CitizenSupportAiResponse generateCitizenSupportAnswer(String prompt) {
        if (!StringUtils.hasText(geminiApiKey)) {
            throw new IllegalStateException("La API key de Gemini no está configurada.");
        }

        try {
            String body = """
                    {
                      "contents": [
                        {
                          "parts": [
                            {
                              "text": %s
                            }
                          ]
                        }
                      ],
                      "generationConfig": {
                        "temperature": 0.2,
                        "responseMimeType": "application/json"
                      }
                    }
                    """.formatted(objectMapper.writeValueAsString(prompt));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_URL.formatted(geminiModel, geminiApiKey)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException(
                        "No se pudo obtener respuesta de Gemini. Status: "
                                + response.statusCode()
                                + ". Response: "
                                + response.body());
            }

            String aiText = extractGeminiText(response.body());
            String cleanJson = cleanJson(aiText);

            return objectMapper.readValue(cleanJson, CitizenSupportAiResponse.class);

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("La respuesta IA fue interrumpida.", exception);
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo generar la respuesta del asistente IA.", exception);
        }
    }

    private void validateCitizenReportRequest(CitizenReportAiRequest request) {
        if (request == null || !StringUtils.hasText(request.description())) {
            throw new IllegalArgumentException("La descripción del reporte es obligatoria para usar la asistencia IA.");
        }
    }

    private String buildCitizenReportPrompt(CitizenReportAiRequest request) {
        return """
                Eres el asistente de IA de Gestionate, una plataforma para reportar incidencias urbanas.

                Tu tarea es ayudar al ciudadano a mejorar su reporte antes de enviarlo.

                Reglas obligatorias:
                - Responde siempre en español.
                - No asignes prioridad. La prioridad solo puede ser confirmada por recepción municipal.
                - No cambies estados del reporte.
                - No derives reportes.
                - No analices visualmente fotos.
                - Solo puedes considerar si existe evidencia o cuántas evidencias hay.
                - No inventes datos que el ciudadano no haya proporcionado.
                - Devuelve únicamente JSON válido, sin markdown, sin explicaciones adicionales.

                Tipos de incidencia posibles:
                - RESIDUOS_ORGANICOS
                - DESMONTE_RESIDUOS_CONSTRUCCION
                - RESIDUOS_PELIGROSOS_QUIMICOS
                - MUEBLES_OBJETOS_VOLUMINOSOS
                - RESIDUOS_COMERCIALES
                - RESIDUOS_AREAS_PUBLICAS_VERDES
                - OTRO_TIPO_RESIDUO

                Datos del reporte ciudadano:
                Descripción: %s
                Tipos seleccionados: %s
                Dirección o referencia: %s
                Latitud: %s
                Longitud: %s
                Tiene evidencia: %s
                Cantidad de evidencias: %s

                Debes responder con esta estructura exacta:
                {
                  "summary": "resumen breve del problema",
                  "suggestedIncidentTypes": ["TIPO_SUGERIDO"],
                  "descriptionImprovement": "mejora sugerida para la descripción",
                  "missingInformation": ["información faltante"],
                  "citizenMessage": "mensaje claro para el ciudadano"
                }
                """.formatted(
                request.description(),
                request.selectedIncidentTypes(),
                request.address(),
                request.latitude(),
                request.longitude(),
                request.hasEvidence(),
                request.evidenceCount());
    }

    private List<String> buildObjectiveMissingInformation(CitizenReportAiRequest request) {
        List<String> missingInformation = new java.util.ArrayList<>();

        if (!StringUtils.hasText(request.description())) {
            missingInformation.add("Descripción del reporte");
        }

        if (request.selectedIncidentTypes() == null || request.selectedIncidentTypes().isEmpty()) {
            missingInformation.add("Tipo de incidencia");
        }

        if (!StringUtils.hasText(request.address())) {
            missingInformation.add("Dirección o referencia de ubicación");
        }

        if (request.latitude() == null || request.longitude() == null) {
            missingInformation.add("Coordenadas de ubicación");
        }

        if (Boolean.FALSE.equals(request.hasEvidence())
                || request.evidenceCount() == null
                || request.evidenceCount() <= 0) {
            missingInformation.add("Evidencia fotográfica");
        }

        return missingInformation;
    }

    private String extractGeminiText(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);

        JsonNode candidates = root.path("candidates");

        if (!candidates.isArray() || candidates.isEmpty()) {
            throw new IllegalStateException("Gemini no devolvió candidates. Response: " + responseBody);
        }

        JsonNode textNode = candidates
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text");

        if (textNode.isMissingNode() || !StringUtils.hasText(textNode.asText())) {
            throw new IllegalStateException("Gemini no devolvió texto válido. Response: " + responseBody);
        }

        return textNode.asText();
    }

    private String cleanJson(String text) {
        return text
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}
