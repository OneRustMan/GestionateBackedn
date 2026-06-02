package com.gestionate.backend.evidences.infrastructure.mapping;

import com.gestionate.backend.evidences.domain.model.Evidence;
import com.gestionate.backend.evidences.interfaces.rest.dto.EvidenceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvidenceMapper {

    @Mapping(target = "reportId", source = "report.id")
    EvidenceResponse toResponse(Evidence evidence);

    List<EvidenceResponse> toResponseList(List<Evidence> evidences);
}
