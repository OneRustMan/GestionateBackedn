package com.gestionate.backend.reports.infrastructure.mapping;

import com.gestionate.backend.reports.domain.model.Report;
import com.gestionate.backend.reports.domain.model.ReportIncidentType;
import com.gestionate.backend.reports.interfaces.rest.dto.IncidentTypeResponse;
import com.gestionate.backend.reports.interfaces.rest.dto.ReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = IncidentTypeMapper.class)
public interface ReportMapper {

    @Mapping(target = "citizenId", source = "citizen.id")
    @Mapping(target = "status", expression = "java(report.getStatus().name())")
    @Mapping(target = "incidentTypes", source = "reportIncidentTypes")
    ReportResponse toResponse(Report report);

    @Mapping(target = "id", source = "incidentType.id")
    @Mapping(target = "name", expression = "java(reportIncidentType.getIncidentType().getName().name())")
    @Mapping(target = "displayName", source = "incidentType.name", qualifiedByName = "toDisplayName")
    @Mapping(target = "active", source = "incidentType.active")
    IncidentTypeResponse toIncidentTypeResponse(ReportIncidentType reportIncidentType);
}
