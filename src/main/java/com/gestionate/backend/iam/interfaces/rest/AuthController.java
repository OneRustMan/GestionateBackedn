package com.gestionate.backend.iam.interfaces.rest;

import com.gestionate.backend.iam.application.UserRegistrationService;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterCitizenRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Registro, login, refresh, logout y logout-all")
public class AuthController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/register/citizen")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse registerCitizen(@Valid @RequestBody RegisterCitizenRequest request) {
        return userRegistrationService.registerCitizen(request);
    }
}
