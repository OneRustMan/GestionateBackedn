package com.gestionate.backend.reports.application;

import com.gestionate.backend.reports.interfaces.rest.dto.IncidentTypeResponse;

import java.util.List;

public interface IIncidentTypeService {

    List<IncidentTypeResponse> findAllActive();
}
