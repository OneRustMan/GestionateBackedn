package com.gestionate.backend.iam.controller;

import com.gestionate.backend.iam.service.IPasswordRecoveryService;
import com.gestionate.backend.iam.dto.ForgotPasswordRequest;
import com.gestionate.backend.iam.dto.PasswordRecoveryResponse;
import com.gestionate.backend.iam.dto.ResetPasswordRequest;
import com.gestionate.backend.iam.service.IAuthenticationService;
import com.gestionate.backend.iam.dto.LoginRequest;
import com.gestionate.backend.iam.dto.LoginResponse;
import com.gestionate.backend.iam.dto.RegisterCleaningOperationsStaffRequest;
import com.gestionate.backend.iam.dto.RegisterMunicipalReceptionistRequest;
import com.gestionate.backend.iam.service.IUserRegistrationService;
import com.gestionate.backend.iam.dto.RegisterCitizenRequest;
import com.gestionate.backend.iam.dto.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Registro, login, refresh, logout y logout-all")
public class AuthController {
    private final IPasswordRecoveryService passwordRecoveryService;
    private final IUserRegistrationService userRegistrationService;
    private final IAuthenticationService authenticationService;

    @Operation(summary = "Registrar ciudadano")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ciudadano registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o contraseñas no coinciden"),
            @ApiResponse(responseCode = "409", description = "Correo o DNI del ciudadano ya registrado")
    })
    @PostMapping("/register/citizen")
    public ResponseEntity<RegisterResponse> registerCitizen(
            @Valid @RequestBody RegisterCitizenRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userRegistrationService.registerCitizen(request));
    }

    @Operation(summary = "Registrar recepcionista municipal")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recepcionista municipal registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o contraseñas no coinciden"),
            @ApiResponse(responseCode = "409", description = "Correo, DNI o código de trabajador ya registrado")
    })
    @PostMapping("/register/receptionist")
    public ResponseEntity<RegisterResponse> registerMunicipalReceptionist(
            @Valid @RequestBody RegisterMunicipalReceptionistRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userRegistrationService.registerMunicipalReceptionist(request));
    }

    @Operation(summary = "Registrar personal del área operativa de limpieza pública")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Personal operativo registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o contraseñas no coinciden"),
            @ApiResponse(responseCode = "409", description = "Correo, DNI o código de trabajador ya registrado")
    })
    @PostMapping("/register/cleaning-staff")
    public ResponseEntity<RegisterResponse> registerCleaningOperationsStaff(
            @Valid @RequestBody RegisterCleaningOperationsStaffRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userRegistrationService.registerCleaningOperationsStaff(request));
    }

    @Operation(summary = "Iniciar sesión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inicio de sesión correcto"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuario o contraseña incorrectos")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @Operation(summary = "Solicitar código de recuperación de contraseña")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Código de recuperación enviado correctamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo recuperar la contraseña")
    })
    @PostMapping("/password/forgot")
    public ResponseEntity<PasswordRecoveryResponse> requestPasswordRecovery(
            @Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(
                passwordRecoveryService.requestPasswordRecovery(request));
    }

    @Operation(summary = "Restablecer contraseña con código de recuperación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Código inválido, expirado o datos incorrectos")
    })
    @PostMapping("/password/reset")
    public ResponseEntity<PasswordRecoveryResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(
                passwordRecoveryService.resetPassword(request));
    }
}
