package com.gestionate.backend.workorders.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompleteWorkOrderRequest(

        @NotBlank(message = "Ingresa una observación para completar la orden.") @Size(max = 500, message = "La observación no puede superar los 500 caracteres.") String observation

) {
}
