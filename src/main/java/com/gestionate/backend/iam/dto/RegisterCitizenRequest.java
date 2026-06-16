package com.gestionate.backend.iam.dto;

import jakarta.validation.constraints.*;

public record RegisterCitizenRequest(

        @NotBlank(message = "Los nombres son obligatorios.") @Size(max = 80, message = "Los nombres no deben superar los 80 caracteres.") String firstName,

        @NotBlank(message = "Los apellidos son obligatorios.") @Size(max = 80, message = "Los apellidos no deben superar los 80 caracteres.") String lastName,

        @NotBlank(message = "El DNI es obligatorio.") @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos.") String dni,

        @NotBlank(message = "El teléfono es obligatorio.") @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos.") String phone,

        @NotBlank(message = "El correo electrónico es obligatorio.") @Email(message = "El correo electrónico no tiene un formato válido.") @Size(max = 120, message = "El correo no debe superar los 120 caracteres.") String email,

        @NotBlank(message = "La contraseña es obligatoria.") @Size(min = 6, max = 80, message = "La contraseña debe tener entre 6 y 80 caracteres.") String password,

        @NotBlank(message = "La confirmación de contraseña es obligatoria.") @Size(min = 6, max = 80, message = "La confirmación de contraseña debe tener entre 6 y 80 caracteres.") String confirmPassword,

        Long districtId,

        @Size(max = 100, message = "El distrito no debe superar los 100 caracteres.") String districtName,

        @Size(max = 100, message = "La provincia no debe superar los 100 caracteres.") String province,

        @NotBlank(message = "La dirección del domicilio es obligatoria.") @Size(max = 180, message = "La dirección no debe superar los 180 caracteres.") String homeAddress) {
}
