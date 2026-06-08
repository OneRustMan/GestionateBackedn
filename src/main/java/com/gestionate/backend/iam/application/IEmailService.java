package com.gestionate.backend.iam.application;

public interface IEmailService {

    void sendPasswordResetCode(String to, String code);
}
