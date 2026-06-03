package com.gestionate.backend.evidences.application;

import com.gestionate.backend.evidences.domain.model.Evidence;
import com.gestionate.backend.evidences.domain.repository.EvidenceRepository;
import com.gestionate.backend.evidences.infrastructure.storage.SupabaseStorageClient;
import com.gestionate.backend.reports.domain.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvidenceService implements IEvidenceService {

    private static final int MAX_FILES = 3;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final EvidenceRepository evidenceRepository;
    private final SupabaseStorageClient supabaseStorageClient;

    @Override
    public List<Evidence> saveReportEvidences(Report report, List<MultipartFile> files) {
        validateFiles(files);

        List<Evidence> evidences = new ArrayList<>();

        for (MultipartFile file : files) {
            validateFile(file);

            String fileUrl = supabaseStorageClient.uploadReportEvidence(report.getId(), file);

            Evidence evidence = Evidence.builder()
                    .report(report)
                    .fileUrl(fileUrl)
                    .fileType(file.getContentType())
                    .uploadedAt(LocalDateTime.now())
                    .build();

            evidences.add(evidence);
        }

        return evidenceRepository.saveAll(evidences);
    }

    private void validateFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe adjuntar al menos una evidencia fotográfica.");
        }

        if (files.size() > MAX_FILES) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo puede adjuntar como máximo 3 evidencias fotográficas.");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La evidencia fotográfica no puede estar vacía.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La evidencia fotográfica no puede superar los 5 MB.");
        }

        String contentType = file.getContentType();

        if (!isAllowedImageType(contentType)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se permiten imágenes JPG, JPEG o PNG.");
        }
    }

    private boolean isAllowedImageType(String contentType) {
        return "image/jpeg".equals(contentType)
                || "image/jpg".equals(contentType)
                || "image/png".equals(contentType);
    }
}
