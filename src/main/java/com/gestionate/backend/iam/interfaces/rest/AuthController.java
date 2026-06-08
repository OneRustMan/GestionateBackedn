package com.gestionate.backend.iam.interfaces.rest;

import com.gestionate.backend.iam.application.IAuthenticationService;
import com.gestionate.backend.iam.interfaces.rest.dto.LoginRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.LoginResponse;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterCleaningOperationsStaffRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterMunicipalReceptionistRequest;
import com.gestionate.backend.iam.application.IUserRegistrationService;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterCitizenRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterResponse;
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
}
