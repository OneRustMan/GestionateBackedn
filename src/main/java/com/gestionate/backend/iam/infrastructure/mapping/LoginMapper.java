package com.gestionate.backend.iam.infrastructure.mapping;

import com.gestionate.backend.iam.domain.model.User;
import com.gestionate.backend.iam.interfaces.rest.dto.LoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    @Mapping(target = "token", source = "token")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "profileId", source = "profileId")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "email", source = "user.email")
    LoginResponse toResponse(
            User user,
            Long profileId,
            String token);
}
