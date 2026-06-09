package com.gestionate.backend.communication.application;

import com.gestionate.backend.iam.domain.model.User;
import com.gestionate.backend.reports.domain.model.ReportStatus;

public interface INotificationService {

    void notifyReportStatusChanged(
            User citizenUser,
            String reportCode,
            ReportStatus reportStatus);
}
