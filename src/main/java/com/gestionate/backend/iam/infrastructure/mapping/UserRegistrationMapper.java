package com.gestionate.backend.iam.infrastructure.mapping;

import com.gestionate.backend.iam.domain.model.Citizen;
import com.gestionate.backend.iam.domain.model.User;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {

    default RegisterResponse toRegisterResponse(User user, Citizen citizen, String message) {
        String fullName = user.getFirstName() + " " + user.getLastName();

        return new RegisterResponse(
                user.getId(),
                citizen.getId(),
                user.getRole(),
                fullName,
                user.getEmail(),
                message);
    }
}
