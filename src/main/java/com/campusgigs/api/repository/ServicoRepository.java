package com.campusgigs.api.repository;

import com.campusgigs.api.model.Servico;
import com.campusgigs.api.model.SituacaoServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findBySituacao(SituacaoServico situacao);
}
