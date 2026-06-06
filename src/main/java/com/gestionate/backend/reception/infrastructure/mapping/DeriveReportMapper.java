package com.gestionate.backend.reception.infrastructure.mapping;

import com.gestionate.backend.reception.interfaces.rest.dto.DeriveReportResponse;
import com.gestionate.backend.workorders.domain.model.WorkOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeriveReportMapper {

    @Mapping(target = "workOrderId", source = "id")
    @Mapping(target = "orderCode", source = "orderCode")
    @Mapping(target = "reportId", source = "report.id")
    @Mapping(target = "reportCode", source = "report.reportCode")
    @Mapping(target = "receptionistId", source = "receptionist.id")
    @Mapping(target = "priority", source = "priority")
    @Mapping(target = "workOrderStatus", source = "status")
    @Mapping(target = "reportStatus", source = "report.status")
    @Mapping(target = "createdAt", source = "createdAt")
    DeriveReportResponse toResponse(WorkOrder workOrder);
}
