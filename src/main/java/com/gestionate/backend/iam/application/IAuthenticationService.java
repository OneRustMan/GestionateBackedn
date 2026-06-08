package com.gestionate.backend.iam.application;

import com.gestionate.backend.iam.interfaces.rest.dto.LoginRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.LoginResponse;

public interface IAuthenticationService {

    LoginResponse login(LoginRequest request);
}
