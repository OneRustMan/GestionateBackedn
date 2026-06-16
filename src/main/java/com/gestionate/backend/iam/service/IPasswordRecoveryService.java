package com.gestionate.backend.iam.service;

import com.gestionate.backend.iam.dto.ForgotPasswordRequest;
import com.gestionate.backend.iam.dto.PasswordRecoveryResponse;
import com.gestionate.backend.iam.dto.ResetPasswordRequest;

public interface IPasswordRecoveryService {

    PasswordRecoveryResponse requestPasswordRecovery(ForgotPasswordRequest request);

    PasswordRecoveryResponse resetPassword(ResetPasswordRequest request);
}
