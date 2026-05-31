package com.gestionate.backend.reports.infrastructure.mapping;

import com.gestionate.backend.reports.domain.model.Report;
import com.gestionate.backend.reports.interfaces.rest.dto.ReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "citizenId", source = "citizen.id")
    @Mapping(target = "status", expression = "java(report.getStatus().name())")
    ReportResponse toResponse(Report report);
}
