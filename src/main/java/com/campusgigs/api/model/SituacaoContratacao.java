package com.campusgigs.api.model;

/**
 * Situação de uma contratação.
 * -----------------------------------------------------------------------
 * Fluxo esperado: SOLICITADA -> ACEITA -> CONCLUIDA
 *                            \-> CANCELADA (em qualquer ponto antes de concluir)
 * -----------------------------------------------------------------------
 */
public enum SituacaoContratacao {
    SOLICITADA,
    ACEITA,
    CONCLUIDA,
    CANCELADA
}
