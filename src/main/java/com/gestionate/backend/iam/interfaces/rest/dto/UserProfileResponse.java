package com.gestionate.backend.iam.interfaces.rest.dto;

import com.gestionate.backend.iam.domain.model.MunicipalUnit;
import com.gestionate.backend.iam.domain.model.Shift;
import com.gestionate.backend.iam.domain.model.UserRole;

public record UserProfileResponse(
        Long userId,
        Long profileId,
        UserRole role,

        String firstName,
        String lastName,
        String dni,
        String phone,
        String email,

        Long districtId,
        String districtName,
        String province,
        String homeAddress,

        Long municipalityId,
        String municipalityName,
        MunicipalUnit municipalUnit,
        String workerCode,
        Shift shift) {
}
