package com.gestionate.backend.ai.service;

import com.gestionate.backend.ai.dto.CitizenReportAiRequest;
import com.gestionate.backend.ai.dto.CitizenReportAiResponse;

public interface IAiService {

    CitizenReportAiResponse assistCitizenReport(CitizenReportAiRequest request);
}
