package com.gestionate.backend.ai.service;

import com.gestionate.backend.ai.dto.AiChatRequest;
import com.gestionate.backend.ai.dto.CitizenSupportAiResponse;
import com.gestionate.backend.ai.dto.RagIngestResponse;

public interface IRagService {

    RagIngestResponse ingestDocuments();

    CitizenSupportAiResponse askCitizenSupport(AiChatRequest request);
}
