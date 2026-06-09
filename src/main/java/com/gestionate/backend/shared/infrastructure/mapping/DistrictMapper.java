package com.gestionate.backend.shared.infrastructure.mapping;

import com.gestionate.backend.shared.domain.model.District;
import com.gestionate.backend.shared.interfaces.rest.dto.DistrictResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DistrictMapper {

    DistrictResponse toResponse(District district);
}
