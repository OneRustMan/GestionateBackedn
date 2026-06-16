package com.gestionate.backend.reception.mapper;

import com.gestionate.backend.reception.dto.DeriveReportResponse;
import com.gestionate.backend.workorders.model.WorkOrder;
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
