package com.gestionate.backend.iam.dto;

import com.gestionate.backend.iam.model.MunicipalUnit;
import com.gestionate.backend.iam.model.Shift;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @NotBlank(message = "Completa los campos obligatorios.") @Size(max = 80, message = "Los nombres no deben superar los 80 caracteres.") String firstName,

        @NotBlank(message = "Completa los campos obligatorios.") @Size(max = 80, message = "Los apellidos no deben superar los 80 caracteres.") String lastName,

        @NotBlank(message = "Completa los campos obligatorios.") @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos.") String phone,

        @NotBlank(message = "Completa los campos obligatorios.") @Email(message = "El correo electrónico no tiene un formato válido.") @Size(max = 120, message = "El correo no debe superar los 120 caracteres.") String email,

        Long districtId,

        @Size(max = 100, message = "El distrito no debe superar los 100 caracteres.") String districtName,

        @Size(max = 100, message = "La provincia no debe superar los 100 caracteres.") String province,

        @Size(max = 180, message = "La dirección no debe superar los 180 caracteres.") String homeAddress,

        Long municipalityId,

        @Size(max = 150, message = "El nombre de la municipalidad no debe superar los 150 caracteres.") String municipalityName,

        MunicipalUnit municipalUnit,

        Shift shift) {
}
