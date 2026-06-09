package com.gestionate.backend.iam.application;

import com.gestionate.backend.iam.interfaces.rest.dto.LogoutResponse;

public interface ISessionService {

    LogoutResponse logout(String email);
}
