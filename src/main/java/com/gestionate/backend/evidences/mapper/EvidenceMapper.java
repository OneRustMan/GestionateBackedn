package com.gestionate.backend.evidences.mapper;

import com.gestionate.backend.evidences.model.Evidence;
import com.gestionate.backend.evidences.dto.EvidenceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvidenceMapper {

    @Mapping(target = "reportId", source = "report.id")
    EvidenceResponse toResponse(Evidence evidence);

    List<EvidenceResponse> toResponseList(List<Evidence> evidences);
}
