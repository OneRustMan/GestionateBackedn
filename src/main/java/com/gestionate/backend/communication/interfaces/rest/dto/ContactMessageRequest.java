package com.gestionate.backend.communication.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record ContactMessageRequest(
        @NotBlank(message = "El nombre es obligatorio") String name,
        @NotBlank @Email(message = "El correo electronico es obligatorio") String email,
        @NotBlank(message = "El asunto es obligatorio") String subject,
        @NotBlank(message = "El mensaje es obligatorio") String message

) {
}
