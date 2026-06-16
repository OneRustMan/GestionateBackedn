package com.gestionate.backend.iam.service;

import com.gestionate.backend.iam.dto.LoginRequest;
import com.gestionate.backend.iam.dto.LoginResponse;

public interface IAuthenticationService {

    LoginResponse login(LoginRequest request);
}
