package com.campusgigs.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PublicarServicoRequest(

        @NotBlank(message = "título é obrigatório")
        @Size(min = 3, max = 120, message = "título deve ter entre 3 e 120 caracteres")
        String titulo,

        @NotBlank(message = "descrição é obrigatória")
        @Size(min = 10, max = 2000, message = "descrição deve ter entre 10 e 2000 caracteres")
        String descricao,

        @NotBlank(message = "categoria é obrigatória")
        String categoria,

        @NotNull(message = "preço é obrigatório")
        @DecimalMin(value = "0.01", message = "preço deve ser maior que zero")
        BigDecimal preco
) {
}
