package com.gestionate.backend.evidences.application;

import com.gestionate.backend.evidences.domain.model.Evidence;
import com.gestionate.backend.reports.domain.model.Report;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IEvidenceService {

    List<Evidence> saveReportEvidences(Report report, List<MultipartFile> files);
}
