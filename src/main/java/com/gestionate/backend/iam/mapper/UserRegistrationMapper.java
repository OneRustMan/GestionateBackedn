package com.gestionate.backend.iam.mapper;

import com.gestionate.backend.iam.model.CleaningOperationsStaff;
import com.gestionate.backend.iam.model.Citizen;
import com.gestionate.backend.iam.model.MunicipalReceptionist;
import com.gestionate.backend.iam.model.User;
import com.gestionate.backend.iam.dto.RegisterResponse;
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

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "profileId", source = "municipalReceptionist.id")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "email", source = "user.email")
    RegisterResponse toRegisterResponse(User user, MunicipalReceptionist municipalReceptionist);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "profileId", source = "cleaningOperationsStaff.id")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "email", source = "user.email")
    RegisterResponse toRegisterResponse(User user, CleaningOperationsStaff cleaningOperationsStaff);
}
