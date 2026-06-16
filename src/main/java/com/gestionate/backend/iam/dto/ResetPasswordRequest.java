package com.gestionate.backend.iam.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(

        @NotBlank(message = "El correo electrónico es obligatorio.") @Email(message = "El correo electrónico no tiene un formato válido.") @Size(max = 120, message = "El correo no debe superar los 120 caracteres.") String email,

        @NotBlank(message = "El código de recuperación es obligatorio.") @Pattern(regexp = "\\d{6}", message = "El código de recuperación debe tener 6 dígitos.") String code,

        @NotBlank(message = "La nueva contraseña es obligatoria.") @Size(min = 6, max = 80, message = "La nueva contraseña debe tener entre 6 y 80 caracteres.") String newPassword,

        @NotBlank(message = "La confirmación de contraseña es obligatoria.") @Size(min = 6, max = 80, message = "La confirmación de contraseña debe tener entre 6 y 80 caracteres.") String confirmPassword

) {
}
