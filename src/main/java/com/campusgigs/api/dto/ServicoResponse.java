package com.campusgigs.api.dto;

import com.campusgigs.api.model.Servico;
import com.campusgigs.api.model.SituacaoServico;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServicoResponse(
        Long id,
        Long prestadorId,
        String prestadorNome,
        String titulo,
        String descricao,
        String categoria,
        BigDecimal preco,
        SituacaoServico situacao,
        LocalDateTime criadoEm
) {

    public static ServicoResponse de(Servico servico) {
        return new ServicoResponse(
                servico.getId(),
                servico.getPrestador().getId(),
                servico.getPrestador().getNome(),
                servico.getTitulo(),
                servico.getDescricao(),
                servico.getCategoria(),
                servico.getPreco(),
                servico.getSituacao(),
                servico.getCriadoEm()
        );
    }
}
