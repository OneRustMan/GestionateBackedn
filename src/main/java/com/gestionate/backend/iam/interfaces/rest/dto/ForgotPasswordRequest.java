package com.gestionate.backend.iam.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordRequest(

        @NotBlank(message = "El correo electrónico es obligatorio.") @Email(message = "El correo electrónico no tiene un formato válido.") @Size(max = 120, message = "El correo no debe superar los 120 caracteres.") String email

) {
}
