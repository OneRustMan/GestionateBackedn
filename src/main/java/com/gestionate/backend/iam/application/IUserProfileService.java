package com.gestionate.backend.iam.application;

import com.gestionate.backend.iam.interfaces.rest.dto.UpdateProfileRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.UserProfileResponse;

public interface IUserProfileService {

    UserProfileResponse getMyProfile(String email);

    UserProfileResponse updateMyProfile(
            String email,
            UpdateProfileRequest request);
}
