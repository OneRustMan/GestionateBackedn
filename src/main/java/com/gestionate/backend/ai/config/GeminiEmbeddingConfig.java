package com.gestionate.backend.ai.config;

import org.springframework.ai.document.Document;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GeminiEmbeddingConfig {

    private final ObjectMapper objectMapper;

    @Value("${ai.gemini.api-key}")
    private String geminiApiKey;

    @Value("${ai.gemini.embedding-model}")
    private String geminiEmbeddingModel;

    @Value("${ai.gemini.embedding-dimensions}")
    private Integer geminiEmbeddingDimensions;

    @Bean
    public EmbeddingModel embeddingModel() {
        return new GeminiRestEmbeddingModel(
                objectMapper,
                geminiApiKey,
                geminiEmbeddingModel,
                geminiEmbeddingDimensions);
    }

    private static class GeminiRestEmbeddingModel implements EmbeddingModel {

        private static final String GEMINI_EMBEDDING_URL = "https://generativelanguage.googleapis.com/v1beta/%s:embedContent?key=%s";

        private final ObjectMapper objectMapper;
        private final String geminiApiKey;
        private final String geminiEmbeddingModel;
        private final Integer geminiEmbeddingDimensions;
        private final HttpClient httpClient = HttpClient.newHttpClient();

        private GeminiRestEmbeddingModel(
                ObjectMapper objectMapper,
                String geminiApiKey,
                String geminiEmbeddingModel,
                Integer geminiEmbeddingDimensions) {

            this.objectMapper = objectMapper;
            this.geminiApiKey = geminiApiKey;
            this.geminiEmbeddingModel = geminiEmbeddingModel;
            this.geminiEmbeddingDimensions = geminiEmbeddingDimensions;
        }

        @Override
        public EmbeddingResponse call(EmbeddingRequest request) {
            List<Embedding> embeddings = new ArrayList<>();

            List<String> instructions = request.getInstructions();

            for (int index = 0; index < instructions.size(); index++) {
                float[] vector = embedText(instructions.get(index));
                embeddings.add(new Embedding(vector, index));
            }

            return new EmbeddingResponse(embeddings);
        }

        private float[] embedText(String text) {
            try {
                String modelPath = normalizeModelPath(geminiEmbeddingModel);

                ObjectNode payload = objectMapper.createObjectNode();
                payload.put("model", modelPath);
                payload.put("outputDimensionality", geminiEmbeddingDimensions);

                ObjectNode content = payload.putObject("content");
                content.putArray("parts")
                        .addObject()
                        .put("text", text);

                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(GEMINI_EMBEDDING_URL.formatted(modelPath, geminiApiKey)))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                        .build();

                HttpResponse<String> response = httpClient.send(
                        httpRequest,
                        HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() < 200 || response.statusCode() >= 300) {
                    throw new IllegalStateException(
                            "No se pudo generar embedding con Gemini. Status: "
                                    + response.statusCode()
                                    + ". Response: "
                                    + response.body());
                }

                JsonNode values = objectMapper.readTree(response.body())
                        .path("embedding")
                        .path("values");

                if (!values.isArray() || values.isEmpty()) {
                    throw new IllegalStateException(
                            "Gemini no devolvió valores de embedding. Response: "
                                    + response.body());
                }

                float[] vector = new float[values.size()];

                for (int i = 0; i < values.size(); i++) {
                    vector[i] = (float) values.get(i).asDouble();
                }

                return vector;

            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("La generación de embeddings fue interrumpida.", exception);
            } catch (Exception exception) {
                throw new IllegalStateException("No se pudo generar embedding con Gemini.", exception);
            }
        }

        @Override
        public float[] embed(Document document) {
            return embedText(document.getText());
        }

        private String normalizeModelPath(String model) {
            if (model.startsWith("models/")) {
                return model;
            }

            return "models/" + model;
        }
    }
}
