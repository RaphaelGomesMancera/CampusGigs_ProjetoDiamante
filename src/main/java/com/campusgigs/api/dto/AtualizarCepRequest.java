package com.campusgigs.api.dto;

import jakarta.validation.constraints.Pattern;

public record AtualizarCepRequest(
        @Pattern(regexp = "\\d{8}", message = "cep deve conter 8 dígitos numéricos, sem hífen")
        String cep
) {
}
