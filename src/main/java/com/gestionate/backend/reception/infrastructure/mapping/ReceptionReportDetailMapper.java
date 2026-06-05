package com.gestionate.backend.reception.infrastructure.mapping;

import com.gestionate.backend.evidences.domain.model.Evidence;
import com.gestionate.backend.evidences.infrastructure.mapping.EvidenceMapper;
import com.gestionate.backend.reception.interfaces.rest.dto.ReceptionReportDetailResponse;
import com.gestionate.backend.reports.domain.model.Location;
import com.gestionate.backend.reports.domain.model.Report;
import com.gestionate.backend.reports.domain.model.ReportIncidentType;
import com.gestionate.backend.reports.infrastructure.mapping.IncidentTypeMapper;
import com.gestionate.backend.reports.infrastructure.mapping.LocationMapper;
import com.gestionate.backend.reports.interfaces.rest.dto.IncidentTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        IncidentTypeMapper.class,
        LocationMapper.class,
        EvidenceMapper.class
})
public interface ReceptionReportDetailMapper {

    @Mapping(target = "reportId", source = "report.id")
    @Mapping(target = "reportCode", source = "report.reportCode")
    @Mapping(target = "description", source = "report.description")
    @Mapping(target = "status", expression = "java(report.getStatus().name())")
    @Mapping(target = "createdAt", source = "report.createdAt")
    @Mapping(target = "updatedAt", source = "report.updatedAt")

    @Mapping(target = "citizenId", source = "report.citizen.id")
    @Mapping(target = "citizenFullName", expression = "java(report.getCitizen().getUser().getFirstName() + \" \" + report.getCitizen().getUser().getLastName())")
    @Mapping(target = "citizenDni", source = "report.citizen.user.dni")
    @Mapping(target = "citizenPhone", source = "report.citizen.user.phone")
    @Mapping(target = "citizenEmail", source = "report.citizen.user.email")

    @Mapping(target = "incidentTypes", source = "report.reportIncidentTypes")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "evidences", source = "evidences")
    ReceptionReportDetailResponse toResponse(
            Report report,
            Location location,
            List<Evidence> evidences);

    @Mapping(target = "id", source = "incidentType.id")
    @Mapping(target = "name", expression = "java(reportIncidentType.getIncidentType().getName().name())")
    @Mapping(target = "displayName", source = "incidentType.name", qualifiedByName = "toDisplayName")
    @Mapping(target = "active", source = "incidentType.active")
    IncidentTypeResponse toIncidentTypeResponse(ReportIncidentType reportIncidentType);
}
