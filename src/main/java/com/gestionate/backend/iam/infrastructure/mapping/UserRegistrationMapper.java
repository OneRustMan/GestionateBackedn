package com.gestionate.backend.iam.infrastructure.mapping;

import com.gestionate.backend.iam.domain.model.Citizen;
import com.gestionate.backend.iam.domain.model.User;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "profileId", source = "citizen.id")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "email", source = "user.email")
    RegisterResponse toRegisterResponse(User user, Citizen citizen);
}
