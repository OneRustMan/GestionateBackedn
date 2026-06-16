package com.gestionate.backend.reports.service;

import com.gestionate.backend.reports.dto.IncidentTypeResponse;

import java.util.List;

public interface IIncidentTypeService {

    List<IncidentTypeResponse> findAllActive();
}
