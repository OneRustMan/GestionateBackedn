package com.gestionate.backend.iam.service;

public interface IEmailService {

    void sendPasswordResetCode(String to, String code);

    void sendReportStatusNotification(String to, String title, String message);

    void sendContactAutoReply(String to, String name, String registerUrl);
}
