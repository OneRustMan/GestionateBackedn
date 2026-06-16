package com.gestionate.backend.iam.controller;

import com.gestionate.backend.iam.service.ISessionService;
import com.gestionate.backend.iam.dto.LogoutResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
@Tag(name = "Session", description = "Gestión de sesión del usuario autenticado")
public class SessionController {

    private final ISessionService sessionService;

    @Operation(summary = "Cerrar sesión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesión cerrada correctamente"),
            @ApiResponse(responseCode = "401", description = "Sesión no válida o usuario no autenticado")
    })
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            Authentication authentication) {
        return ResponseEntity.ok(
                sessionService.logout(authentication.getName()));
    }
}
