package com.gestionate.backend.communication.service;

import com.gestionate.backend.iam.model.User;
import com.gestionate.backend.reports.model.ReportStatus;

public interface INotificationService {

    void notifyReportStatusChanged(
            User citizenUser,
            String reportCode,
            ReportStatus reportStatus);
}
