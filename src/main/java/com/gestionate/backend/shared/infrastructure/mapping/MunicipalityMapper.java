package com.gestionate.backend.shared.infrastructure.mapping;

import com.gestionate.backend.shared.domain.model.Municipality;
import com.gestionate.backend.shared.interfaces.rest.dto.MunicipalityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MunicipalityMapper {

    @Mapping(target = "districtId", source = "district.id")
    @Mapping(target = "districtName", source = "district.name")
    @Mapping(target = "province", source = "district.province")
    MunicipalityResponse toResponse(Municipality municipality);
}
