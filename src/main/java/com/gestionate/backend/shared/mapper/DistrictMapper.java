package com.gestionate.backend.shared.mapper;

import com.gestionate.backend.shared.model.District;
import com.gestionate.backend.shared.dto.DistrictResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DistrictMapper {

    DistrictResponse toResponse(District district);
}
