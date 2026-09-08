package com.campusgigs.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Dados de entrada para cadastrar um novo usuário.
 * `cep` é opcional no cadastro — se vier, a API já tenta resolver
 * cidade/UF via HttpExchange (integração que entra no CP5).
 */
public record RegistrarUsuarioRequest(

        @NotBlank(message = "nome é obrigatório")
        @Size(min = 2, max = 120, message = "nome deve ter entre 2 e 120 caracteres")
        String nome,

        @NotBlank(message = "e-mail é obrigatório")
        @Email(message = "e-mail em formato inválido")
        String email,

        @NotBlank(message = "senha é obrigatória")
        @Size(min = 6, max = 100, message = "senha deve ter no mínimo 6 caracteres")
        String senha,

        @Pattern(regexp = "\\d{8}", message = "cep deve conter 8 dígitos numéricos, sem hífen")
        String cep
) {
}
