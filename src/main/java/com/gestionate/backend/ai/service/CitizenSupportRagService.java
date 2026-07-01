package com.gestionate.backend.ai.service;

import com.gestionate.backend.ai.dto.AiChatRequest;
import com.gestionate.backend.ai.dto.CitizenSupportAiResponse;
import com.gestionate.backend.ai.dto.RagIngestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitizenSupportRagService implements IRagService {

    private final VectorStore vectorStore;
    private final GeminiAiService geminiAiService;

    @Override
    public RagIngestResponse ingestDocuments() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:docs/*.pdf");

            List<Document> documents = new ArrayList<>();

            for (Resource resource : resources) {
                PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);
                List<Document> pdfDocuments = reader.get();

                for (Document document : pdfDocuments) {
                    document.getMetadata().put("source", resource.getFilename());
                    documents.add(document);
                }
            }

            List<Document> chunks = new TokenTextSplitter().apply(documents);

            vectorStore.add(chunks);

            log.info("Documentos RAG ingestados correctamente. Chunks: {}", chunks.size());

            return new RagIngestResponse(chunks.size());

        } catch (IOException exception) {
            throw new IllegalStateException("No se pudieron leer los documentos RAG.", exception);
        } catch (Exception exception) {
            log.error("Error completo durante la ingesta RAG.", exception);
            throw new IllegalStateException(
                    "No se pudo realizar la ingesta RAG. Causa: " + exception.getMessage(),
                    exception);
        }
    }

    @Override
    public CitizenSupportAiResponse askCitizenSupport(AiChatRequest request) {
        if (request == null || !StringUtils.hasText(request.message())) {
            throw new IllegalArgumentException("La pregunta es obligatoria.");
        }

        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(request.message())
                        .topK(4)
                        .build());

        if (documents == null || documents.isEmpty()) {
            return new CitizenSupportAiResponse(
                    "No encontré información suficiente en las guías de Gestionate para responder esa consulta sin inventar datos.",
                    false);
        }

        String context = buildContext(documents);
        String prompt = buildSupportPrompt(request.message(), context);

        return geminiAiService.generateCitizenSupportAnswer(prompt);
    }

    private String buildContext(List<Document> documents) {
        StringBuilder context = new StringBuilder();

        for (Document document : documents) {
            String source = String.valueOf(
                    document.getMetadata().getOrDefault("source", "Documento interno"));

            context.append("Documento: ")
                    .append(source)
                    .append("\n")
                    .append(document.getText())
                    .append("\n\n---\n\n");
        }

        return context.toString();
    }

    private String buildSupportPrompt(String question, String context) {
        return """
                Eres el asistente IA de ayuda de Gestionate.

                Tu tarea es responder preguntas del ciudadano sobre el uso del sistema Gestionate.

                Reglas obligatorias:
                - Responde siempre en español.
                - Usa únicamente la información del contexto autorizado.
                - No inventes funcionalidades, permisos, estados ni tiempos de atención.
                - No prometas que una incidencia será atendida en un tiempo específico.
                - Respeta los roles del sistema.
                - Si el contexto no contiene la respuesta, indica que no tienes información suficiente.
                - No menciones fuentes, documentos ni nombres de archivos al ciudadano.
                - Devuelve únicamente JSON válido, sin markdown ni texto adicional.

                Contexto autorizado:
                %s

                Pregunta del ciudadano:
                %s

                Responde con esta estructura exacta:
                {
                  "reply": "respuesta clara para el ciudadano",
                  "answeredFromDocs": true
                }
                """.formatted(context, question);
    }
}
