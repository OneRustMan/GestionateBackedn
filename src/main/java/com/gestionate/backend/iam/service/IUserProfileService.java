package com.gestionate.backend.iam.service;

import com.gestionate.backend.iam.dto.UpdateProfileRequest;
import com.gestionate.backend.iam.dto.UserProfileResponse;

public interface IUserProfileService {

    UserProfileResponse getMyProfile(String email);

    UserProfileResponse updateMyProfile(
            String email,
            UpdateProfileRequest request);
}
