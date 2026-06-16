package com.gestionate.backend.iam.mapper;

import com.gestionate.backend.iam.model.Citizen;
import com.gestionate.backend.iam.model.CleaningOperationsStaff;
import com.gestionate.backend.iam.model.MunicipalReceptionist;
import com.gestionate.backend.iam.model.User;
import com.gestionate.backend.iam.dto.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "profileId", source = "citizen.id")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "dni", source = "user.dni")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "districtId", source = "citizen.district.id")
    @Mapping(target = "districtName", source = "citizen.district.name")
    @Mapping(target = "province", source = "citizen.district.province")
    @Mapping(target = "homeAddress", source = "citizen.homeAddress")
    @Mapping(target = "municipalityId", ignore = true)
    @Mapping(target = "municipalityName", ignore = true)
    @Mapping(target = "municipalUnit", ignore = true)
    @Mapping(target = "workerCode", ignore = true)
    @Mapping(target = "shift", ignore = true)
    UserProfileResponse toResponse(User user, Citizen citizen);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "profileId", source = "municipalReceptionist.id")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "dni", source = "user.dni")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "districtId", source = "municipalReceptionist.municipality.district.id")
    @Mapping(target = "districtName", source = "municipalReceptionist.municipality.district.name")
    @Mapping(target = "province", source = "municipalReceptionist.municipality.district.province")
    @Mapping(target = "homeAddress", ignore = true)
    @Mapping(target = "municipalityId", source = "municipalReceptionist.municipality.id")
    @Mapping(target = "municipalityName", source = "municipalReceptionist.municipality.name")
    @Mapping(target = "municipalUnit", source = "municipalReceptionist.municipalUnit")
    @Mapping(target = "workerCode", source = "municipalReceptionist.workerCode")
    @Mapping(target = "shift", ignore = true)
    UserProfileResponse toResponse(User user, MunicipalReceptionist municipalReceptionist);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "profileId", source = "cleaningOperationsStaff.id")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "dni", source = "user.dni")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "districtId", source = "cleaningOperationsStaff.municipality.district.id")
    @Mapping(target = "districtName", source = "cleaningOperationsStaff.municipality.district.name")
    @Mapping(target = "province", source = "cleaningOperationsStaff.municipality.district.province")
    @Mapping(target = "homeAddress", ignore = true)
    @Mapping(target = "municipalityId", source = "cleaningOperationsStaff.municipality.id")
    @Mapping(target = "municipalityName", source = "cleaningOperationsStaff.municipality.name")
    @Mapping(target = "municipalUnit", ignore = true)
    @Mapping(target = "workerCode", source = "cleaningOperationsStaff.workerCode")
    @Mapping(target = "shift", source = "cleaningOperationsStaff.shift")
    UserProfileResponse toResponse(User user, CleaningOperationsStaff cleaningOperationsStaff);
}
