package com.campusgigs.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * GlobalExceptionHandler
 * -----------------------------------------------------------------------
 * Único ponto que traduz exceptions em respostas HTTP. Cada tipo de
 * exception de domínio mapeia para um status code específico — nunca
 * deixamos uma exception genérica virar 500 com stack trace exposto.
 * -----------------------------------------------------------------------
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErroResposta.de(404, "Recurso não encontrado", ex.getMessage()));
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResposta> tratarRegraDeNegocio(RegraDeNegocioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErroResposta.de(409, "Regra de negócio violada", ex.getMessage()));
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErroResposta> tratarAcessoNegado(AcessoNegadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErroResposta.de(403, "Acesso negado", ex.getMessage()));
    }

    /** Lançada pelo Spring Security quando @PreAuthorize barra uma operação (CP4). */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResposta> tratarAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErroResposta.de(403, "Acesso negado", "Você não tem papel suficiente para essa operação."));
    }

    /** E-mail/senha inválidos no login — nunca revelamos qual dos dois está errado. */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResposta> tratarCredenciaisInvalidas(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErroResposta.de(401, "Credenciais inválidas", "E-mail ou senha incorretos."));
    }

    /** Falhas de @Valid nos DTOs de request (ex: preço negativo, e-mail em formato inválido). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErroResposta.de(400, "Dados inválidos", "Um ou mais campos não passaram na validação.", detalhes));
    }

    /** Último fallback — qualquer coisa não mapeada vira 500 genérico, nunca a stack trace crua. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> tratarErroGenerico(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErroResposta.de(500, "Erro interno", "Ocorreu um erro inesperado. Tente novamente mais tarde."));
    }
}
