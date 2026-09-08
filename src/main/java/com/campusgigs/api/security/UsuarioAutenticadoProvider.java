package com.campusgigs.api.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Lê o e-mail do usuário autenticado a partir do SecurityContext.
 * O JwtAuthFilter usa o e-mail como "name" da autenticação — ver
 * JwtAuthFilter.doFilterInternal.
 */
@Component
public class UsuarioAutenticadoProvider {

    public String emailAtual() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
