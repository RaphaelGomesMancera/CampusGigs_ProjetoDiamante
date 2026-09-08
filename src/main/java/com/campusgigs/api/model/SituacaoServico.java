package com.campusgigs.api.model;

/**
 * Situação de um serviço (freela) publicado.
 * -----------------------------------------------------------------------
 * Regra de negócio ligada a esse enum (ver ContratacaoService, CP4):
 * só é possível contratar um serviço que esteja ATIVO.
 * -----------------------------------------------------------------------
 */
public enum SituacaoServico {
    ATIVO,
    PAUSADO,
    ENCERRADO
}
