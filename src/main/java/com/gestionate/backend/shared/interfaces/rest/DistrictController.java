package com.gestionate.backend.shared.interfaces.rest;

import com.gestionate.backend.shared.application.IDistrictService;
import com.gestionate.backend.shared.interfaces.rest.dto.DistrictResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
@Tag(name = "Districts", description = "Consulta de distritos disponibles")
public class DistrictController {

    private final IDistrictService districtService;

    @Operation(summary = "Listar distritos activos")
    @GetMapping
    public ResponseEntity<List<DistrictResponse>> findActiveDistricts() {
        return ResponseEntity.ok(
                districtService.findActiveDistricts());
    }
}
