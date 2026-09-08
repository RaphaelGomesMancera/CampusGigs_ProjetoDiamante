package com.campusgigs.api.repository;

import com.campusgigs.api.model.Contratacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratacaoRepository extends JpaRepository<Contratacao, Long> {

    List<Contratacao> findByContratanteId(Long contratanteId);

    List<Contratacao> findByServicoId(Long servicoId);
}
