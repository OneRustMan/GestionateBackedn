package com.gestionate.backend.reception.application;

import com.gestionate.backend.reception.interfaces.rest.dto.DeriveReportRequest;
import com.gestionate.backend.reception.interfaces.rest.dto.DeriveReportResponse;
import com.gestionate.backend.reception.interfaces.rest.dto.ReceptionReportDetailResponse;
import com.gestionate.backend.reception.interfaces.rest.dto.ReceptionReportInboxResponse;

import java.util.List;

public interface IReceptionReportService {

    List<ReceptionReportInboxResponse> findReportInbox(
            Long receptionistId,
            Long incidentTypeId);

    ReceptionReportDetailResponse findReportDetail(
            Long receptionistId,
            Long reportId);

    DeriveReportResponse deriveReport(
            Long receptionistId,
            Long reportId,
            DeriveReportRequest request);

    List<ReceptionReportInboxResponse> findDerivedReports(
            Long receptionistId,
            Long incidentTypeId);
}
