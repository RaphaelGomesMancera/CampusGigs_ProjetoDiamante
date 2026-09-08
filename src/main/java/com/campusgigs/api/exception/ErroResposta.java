package com.campusgigs.api.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ErroResposta
 * -----------------------------------------------------------------------
 * Formato único de erro devolvido pela API inteira. O enunciado pede
 * "resposta clara e centralizada, sem expor detalhes internos como stack
 * traces" — é exatamente o que esse record + o GlobalExceptionHandler
 * garantem: nenhuma exception "crua" do Spring/Hibernate chega no cliente.
 * -----------------------------------------------------------------------
 */
public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        List<String> detalhes
) {

    public static ErroResposta de(int status, String erro, String mensagem) {
        return new ErroResposta(LocalDateTime.now(), status, erro, mensagem, List.of());
    }

    public static ErroResposta de(int status, String erro, String mensagem, List<String> detalhes) {
        return new ErroResposta(LocalDateTime.now(), status, erro, mensagem, detalhes);
    }
}
