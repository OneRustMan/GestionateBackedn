package com.gestionate.backend.shared.interfaces.rest;

import com.gestionate.backend.shared.application.IMunicipalityService;
import com.gestionate.backend.shared.interfaces.rest.dto.MunicipalityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/municipalities")
@RequiredArgsConstructor
@Tag(name = "Municipalities", description = "Consulta de municipalidades disponibles")
public class MunicipalityController {

    private final IMunicipalityService municipalityService;

    @Operation(summary = "Listar municipalidades activas")
    @GetMapping
    public ResponseEntity<List<MunicipalityResponse>> findActiveMunicipalities() {
        return ResponseEntity.ok(
                municipalityService.findActiveMunicipalities());
    }
}
