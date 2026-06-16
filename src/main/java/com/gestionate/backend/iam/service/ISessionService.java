package com.gestionate.backend.iam.service;

import com.gestionate.backend.iam.dto.LogoutResponse;

public interface ISessionService {

    LogoutResponse logout(String email);
}
