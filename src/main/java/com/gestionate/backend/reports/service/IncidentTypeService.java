package com.gestionate.backend.reports.service;

import com.gestionate.backend.reports.model.IncidentType;
import com.gestionate.backend.reports.repository.IncidentTypeRepository;
import com.gestionate.backend.reports.mapper.IncidentTypeMapper;
import com.gestionate.backend.reports.dto.IncidentTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IncidentTypeService implements IIncidentTypeService {

    private final IncidentTypeRepository incidentTypeRepository;
    private final IncidentTypeMapper incidentTypeMapper;

    @Override
    public List<IncidentTypeResponse> findAllActive() {
        List<IncidentType> incidentTypes = incidentTypeRepository.findByActiveTrue();
        return incidentTypeMapper.toResponseList(incidentTypes);
    }

    public List<IncidentType> findActiveIncidentTypesByIds(List<Long> incidentTypeIds) {
        if (incidentTypeIds == null || incidentTypeIds.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe seleccionar al menos un tipo de incidencia.");
        }

        Set<Long> uniqueIds = new LinkedHashSet<>(incidentTypeIds);

        if (uniqueIds.size() != incidentTypeIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No debe enviar tipos de incidencia duplicados.");
        }

        List<IncidentType> incidentTypes = incidentTypeRepository.findAllById(uniqueIds);

        if (incidentTypes.size() != uniqueIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Uno o más tipos de incidencia no existen.");
        }

        boolean hasInactiveIncidentType = incidentTypes.stream()
                .anyMatch(incidentType -> !Boolean.TRUE.equals(incidentType.getActive()));

        if (hasInactiveIncidentType) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Uno o más tipos de incidencia no están activos.");
        }

        return incidentTypes;
    }
}
