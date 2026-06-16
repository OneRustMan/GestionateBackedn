package com.gestionate.backend.evidences.service;

import com.gestionate.backend.evidences.model.Evidence;
import com.gestionate.backend.reports.model.Report;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IEvidenceService {

    List<Evidence> saveReportEvidences(Report report, List<MultipartFile> files);
}
