package com.gestionate.backend.iam.controller;

import com.gestionate.backend.iam.service.IUserProfileService;
import com.gestionate.backend.iam.dto.UpdateProfileRequest;
import com.gestionate.backend.iam.dto.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Vista y actualización del perfil del usuario autenticado")
public class UserProfileController {

    private final IUserProfileService userProfileService;

    @Operation(summary = "Obtener perfil del usuario autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping
    public ResponseEntity<UserProfileResponse> getMyProfile(
            Authentication authentication) {
        return ResponseEntity.ok(
                userProfileService.getMyProfile(authentication.getName()));
    }

    @Operation(summary = "Actualizar perfil del usuario autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Completa los campos obligatorios"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PatchMapping
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(
                userProfileService.updateMyProfile(
                        authentication.getName(),
                        request));
    }
}
