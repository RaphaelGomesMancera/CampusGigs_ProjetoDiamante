package com.campusgigs.api.exception;

/**
 * Lançada quando o usuário está autenticado, mas não tem permissão para a
 * operação (ex: tentar encerrar o serviço de outro usuário sem ser ADMIN).
 * Resulta em HTTP 403 (Forbidden) — diferente de 401 (não autenticado).
 */
public class AcessoNegadoException extends RuntimeException {

    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}
