package com.gestionate.backend.communication.interfaces.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestionate.backend.communication.application.IContactMessageService;
import com.gestionate.backend.communication.interfaces.rest.dto.ContactMessageRequest;
import com.gestionate.backend.communication.interfaces.rest.dto.ContactMessageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contact-messages")
@RequiredArgsConstructor
@Tag(name = "Contact Messages", description = "Mensajes enviados desde la landing page")
public class ContactMessageController {
    private final IContactMessageService contactMessageService;

    @Operation(summary = "Enviar mensaje desde la landing page")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mensaje enviado correctamente desde la landing page"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones")
    })
    @PostMapping
    public ResponseEntity<ContactMessageResponse> sendMessage(@Valid @RequestBody ContactMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactMessageService.sendMessage(request));
    }
}
