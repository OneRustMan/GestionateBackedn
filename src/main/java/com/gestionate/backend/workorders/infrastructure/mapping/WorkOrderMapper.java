package com.gestionate.backend.workorders.infrastructure.mapping;

import com.gestionate.backend.reports.domain.model.Location;
import com.gestionate.backend.reports.domain.model.ReportIncidentType;
import com.gestionate.backend.reports.infrastructure.mapping.IncidentTypeMapper;
import com.gestionate.backend.reports.infrastructure.mapping.LocationMapper;
import com.gestionate.backend.reports.interfaces.rest.dto.IncidentTypeResponse;
import com.gestionate.backend.workorders.domain.model.WorkOrder;
import com.gestionate.backend.workorders.interfaces.rest.dto.WorkOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        IncidentTypeMapper.class,
        LocationMapper.class
})
public interface WorkOrderMapper {

    @Mapping(target = "workOrderId", source = "workOrder.id")
    @Mapping(target = "orderCode", source = "workOrder.orderCode")

    @Mapping(target = "reportId", source = "workOrder.report.id")
    @Mapping(target = "reportCode", source = "workOrder.report.reportCode")
    @Mapping(target = "description", source = "workOrder.report.description")
    @Mapping(target = "reportStatus", source = "workOrder.report.status")

    @Mapping(target = "priority", source = "workOrder.priority")
    @Mapping(target = "workOrderStatus", source = "workOrder.status")

    @Mapping(target = "incidentTypes", source = "workOrder.report.reportIncidentTypes")
    @Mapping(target = "location", source = "location")

    @Mapping(target = "createdAt", source = "workOrder.createdAt")
    WorkOrderResponse toResponse(WorkOrder workOrder, Location location);

    @Mapping(target = "id", source = "incidentType.id")
    @Mapping(target = "name", expression = "java(reportIncidentType.getIncidentType().getName().name())")
    @Mapping(target = "displayName", source = "incidentType.name", qualifiedByName = "toDisplayName")
    @Mapping(target = "active", source = "incidentType.active")
    IncidentTypeResponse toIncidentTypeResponse(ReportIncidentType reportIncidentType);
}
