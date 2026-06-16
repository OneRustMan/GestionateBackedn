package com.gestionate.backend.iam.dto;

import com.gestionate.backend.iam.model.MunicipalUnit;
import com.gestionate.backend.iam.model.Shift;
import com.gestionate.backend.iam.model.UserRole;

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
