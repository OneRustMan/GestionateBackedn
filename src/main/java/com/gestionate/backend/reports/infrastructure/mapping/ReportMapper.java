package com.gestionate.backend.reports.infrastructure.mapping;

import com.gestionate.backend.evidences.domain.model.Evidence;
import com.gestionate.backend.evidences.infrastructure.mapping.EvidenceMapper;
import com.gestionate.backend.reports.domain.model.Report;
import com.gestionate.backend.reports.domain.model.ReportIncidentType;
import com.gestionate.backend.reports.interfaces.rest.dto.IncidentTypeResponse;
import com.gestionate.backend.reports.interfaces.rest.dto.ReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        IncidentTypeMapper.class,
        EvidenceMapper.class
})
public interface ReportMapper {

    @Mapping(target = "citizenId", source = "report.citizen.id")
    @Mapping(target = "status", expression = "java(report.getStatus().name())")
    @Mapping(target = "incidentTypes", source = "report.reportIncidentTypes")
    @Mapping(target = "evidences", source = "evidences")
    ReportResponse toResponse(Report report, List<Evidence> evidences);

    @Mapping(target = "id", source = "incidentType.id")
    @Mapping(target = "name", expression = "java(reportIncidentType.getIncidentType().getName().name())")
    @Mapping(target = "displayName", source = "incidentType.name", qualifiedByName = "toDisplayName")
    @Mapping(target = "active", source = "incidentType.active")
    IncidentTypeResponse toIncidentTypeResponse(ReportIncidentType reportIncidentType);
}
