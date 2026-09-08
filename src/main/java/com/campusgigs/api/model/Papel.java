package com.campusgigs.api.model;

/**
 * Papel do usuário dentro da plataforma.
 * -----------------------------------------------------------------------
 * Usado tanto na coluna `usuarios.papel` quanto nas anotações de
 * autorização do Spring Security (ROLE_ADMIN / ROLE_USER) a partir do CP4.
 * -----------------------------------------------------------------------
 */
public enum Papel {
    ADMIN,
    USER
}
