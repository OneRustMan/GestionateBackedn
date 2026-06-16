package com.gestionate.backend.iam.service;

import com.gestionate.backend.iam.dto.RegisterCitizenRequest;
import com.gestionate.backend.iam.dto.RegisterCleaningOperationsStaffRequest;
import com.gestionate.backend.iam.dto.RegisterMunicipalReceptionistRequest;
import com.gestionate.backend.iam.dto.RegisterResponse;

public interface IUserRegistrationService {

    RegisterResponse registerCitizen(RegisterCitizenRequest request);

    RegisterResponse registerMunicipalReceptionist(RegisterMunicipalReceptionistRequest request);

    RegisterResponse registerCleaningOperationsStaff(RegisterCleaningOperationsStaffRequest request);
}
