package com.gestionate.backend.evidences.storage;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SupabaseStorageClient {

    private final RestClient.Builder restClientBuilder;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    @PostConstruct
    public void checkConfig() {
        System.out.println("SUPABASE_URL=[" + supabaseUrl + "]");
        System.out.println("SUPABASE_BUCKET=[" + bucket + "]");
    }

    public String uploadReportEvidence(Long reportId, MultipartFile file) {
        String filePath = buildFilePath(reportId, file);
        String uploadUrl = buildUploadUrl(filePath);

        try {
            restClientBuilder.build()
                    .post()
                    .uri(uploadUrl)
                    .header("Authorization", "Bearer " + supabaseKey)
                    .header("apikey", supabaseKey)
                    .header("x-upsert", "false")
                    .contentType(resolveContentType(file))
                    .body(file.getBytes())
                    .retrieve()
                    .toBodilessEntity();

            return buildPublicUrl(filePath);

        } catch (IOException exception) {
            throw new IllegalArgumentException("No se pudo leer el archivo de evidencia.");
        }
    }

    private String buildFilePath(Long reportId, MultipartFile file) {
        String extension = getFileExtension(file.getOriginalFilename());
        return "reports/" + reportId + "/" + UUID.randomUUID() + extension;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }

    private String buildUploadUrl(String filePath) {
        if (supabaseUrl == null || supabaseUrl.isBlank()) {
            throw new IllegalStateException("SUPABASE_URL no está configurado.");
        }

        if (!supabaseUrl.startsWith("http://") && !supabaseUrl.startsWith("https://")) {
            throw new IllegalStateException("SUPABASE_URL debe empezar con http:// o https://");
        }

        return supabaseUrl + "/storage/v1/object/" + bucket + "/" + filePath;
    }

    private String buildPublicUrl(String filePath) {
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + filePath;
    }

    private MediaType resolveContentType(MultipartFile file) {
        if (file.getContentType() == null) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }

        return MediaType.parseMediaType(file.getContentType());
    }
}
