package com.campusgigs.api.exception;

/** Lançada quando um recurso (usuário, serviço, contratação) não existe. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
