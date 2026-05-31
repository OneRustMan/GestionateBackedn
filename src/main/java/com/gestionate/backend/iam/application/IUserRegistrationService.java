package com.gestionate.backend.iam.application;

import com.gestionate.backend.iam.interfaces.rest.dto.RegisterCitizenRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterResponse;

public interface IUserRegistrationService {

    RegisterResponse registerCitizen(RegisterCitizenRequest request);
}
