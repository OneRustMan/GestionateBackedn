package com.gestionate.backend.reception.infrastructure.mapping;

import com.gestionate.backend.reception.interfaces.rest.dto.ReceptionReportInboxResponse;
import com.gestionate.backend.reports.domain.model.Location;
import com.gestionate.backend.reports.domain.model.Report;
import com.gestionate.backend.reports.domain.model.ReportIncidentType;
import com.gestionate.backend.reports.infrastructure.mapping.IncidentTypeMapper;
import com.gestionate.backend.reports.infrastructure.mapping.LocationMapper;
import com.gestionate.backend.reports.interfaces.rest.dto.IncidentTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        IncidentTypeMapper.class,
        LocationMapper.class
})
public interface ReceptionReportInboxMapper {

    @Mapping(target = "reportId", source = "report.id")
    @Mapping(target = "reportCode", source = "report.reportCode")
    @Mapping(target = "citizenId", source = "report.citizen.id")
    @Mapping(target = "citizenFullName", expression = "java(report.getCitizen().getUser().getFirstName() + \" \" + report.getCitizen().getUser().getLastName())")
    @Mapping(target = "description", source = "report.description")
    @Mapping(target = "status", expression = "java(report.getStatus().name())")
    @Mapping(target = "incidentTypes", source = "report.reportIncidentTypes")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "createdAt", source = "report.createdAt")
    ReceptionReportInboxResponse toResponse(Report report, Location location);

    @Mapping(target = "id", source = "incidentType.id")
    @Mapping(target = "name", expression = "java(reportIncidentType.getIncidentType().getName().name())")
    @Mapping(target = "displayName", source = "incidentType.name", qualifiedByName = "toDisplayName")
    @Mapping(target = "active", source = "incidentType.active")
    IncidentTypeResponse toIncidentTypeResponse(ReportIncidentType reportIncidentType);
}
