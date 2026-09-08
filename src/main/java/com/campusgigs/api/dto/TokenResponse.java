package com.campusgigs.api.dto;

/** Resposta do login: token de acesso + tipo, no padrão "Bearer <token>". */
public record TokenResponse(String tokenAcesso, String tipo) {

    public static TokenResponse bearer(String token) {
        return new TokenResponse(token, "Bearer");
    }
}
