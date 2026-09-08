package com.campusgigs.api.exception;

/**
 * Lançada quando uma regra de negócio é violada — ex: e-mail já cadastrado,
 * tentar contratar o próprio serviço, contratar um serviço não ativo.
 * Sempre resulta em HTTP 409 (Conflict) ou 400 (Bad Request), nunca 500.
 */
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
