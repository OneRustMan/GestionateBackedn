package com.gestionate.backend.reports.infrastructure.mapping;

import com.gestionate.backend.reports.domain.model.Location;
import com.gestionate.backend.reports.interfaces.rest.dto.LocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "reportId", source = "report.id")
    @Mapping(target = "districtId", source = "district.id")
    @Mapping(target = "districtName", source = "district.name")
    @Mapping(target = "province", source = "district.province")
    LocationResponse toResponse(Location location);
}
