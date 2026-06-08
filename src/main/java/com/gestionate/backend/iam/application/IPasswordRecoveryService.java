package com.gestionate.backend.iam.application;

import com.gestionate.backend.iam.interfaces.rest.dto.ForgotPasswordRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.PasswordRecoveryResponse;
import com.gestionate.backend.iam.interfaces.rest.dto.ResetPasswordRequest;

public interface IPasswordRecoveryService {

    PasswordRecoveryResponse requestPasswordRecovery(ForgotPasswordRequest request);

    PasswordRecoveryResponse resetPassword(ResetPasswordRequest request);
}
