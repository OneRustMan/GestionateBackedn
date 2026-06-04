package com.gestionate.backend.reception.application;

import com.gestionate.backend.reception.interfaces.rest.dto.ReceptionReportInboxResponse;

import java.util.List;

public interface IReceptionReportService {

    List<ReceptionReportInboxResponse> findReportInbox(Long receptionistId);
}
