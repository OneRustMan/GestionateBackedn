package com.gestionate.backend.reports.mapper;

import com.gestionate.backend.reports.model.IncidentType;
import com.gestionate.backend.reports.model.IncidentTypeName;
import com.gestionate.backend.reports.dto.IncidentTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IncidentTypeMapper {

    @Mapping(target = "name", expression = "java(incidentType.getName().name())")
    @Mapping(target = "displayName", source = "name", qualifiedByName = "toDisplayName")
    IncidentTypeResponse toResponse(IncidentType incidentType);

    List<IncidentTypeResponse> toResponseList(List<IncidentType> incidentTypes);

    @Named("toDisplayName")
    @ValueMappings({
            @ValueMapping(source = "RESIDUOS_ORGANICOS", target = "Residuos orgánicos"),
            @ValueMapping(source = "DESMONTE_RESIDUOS_CONSTRUCCION", target = "Desmonte o residuos de construcción"),
            @ValueMapping(source = "RESIDUOS_PELIGROSOS_QUIMICOS", target = "Residuos peligrosos o químicos"),
            @ValueMapping(source = "MUEBLES_OBJETOS_VOLUMINOSOS", target = "Muebles u objetos voluminosos"),
            @ValueMapping(source = "RESIDUOS_COMERCIALES", target = "Residuos comerciales"),
            @ValueMapping(source = "RESIDUOS_AREAS_PUBLICAS_VERDES", target = "Residuos en áreas públicas o verdes"),
            @ValueMapping(source = "OTRO_TIPO_RESIDUO", target = "Otro tipo de residuo")
    })
    String toDisplayName(IncidentTypeName name);
}
