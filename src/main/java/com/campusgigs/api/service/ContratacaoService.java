package com.campusgigs.api.service;

import com.campusgigs.api.exception.RegraDeNegocioException;
import com.campusgigs.api.model.Contratacao;
import com.campusgigs.api.model.Servico;
import com.campusgigs.api.model.Usuario;
import com.campusgigs.api.repository.ContratacaoRepository;
import com.campusgigs.api.security.UsuarioAutenticadoProvider;
import org.springframework.stereotype.Service;

/**
 * ContratacaoService (CP3)
 * -----------------------------------------------------------------------
 * A checagem de "serviço precisa estar ATIVO" é regra de negócio pura
 * (não depende de papel), por isso já entra aqui no CP3. A checagem de
 * "não pode contratar o próprio serviço" é sobre identidade/papel e fica
 * para o CP4, junto com as outras regras de autorização.
 * -----------------------------------------------------------------------
 */
@Service
public class ContratacaoService {

    private final ContratacaoRepository contratacaoRepository;
    private final ServicoService servicoService;
    private final UsuarioService usuarioService;
    private final UsuarioAutenticadoProvider usuarioAutenticadoProvider;

    public ContratacaoService(
            ContratacaoRepository contratacaoRepository,
            ServicoService servicoService,
            UsuarioService usuarioService,
            UsuarioAutenticadoProvider usuarioAutenticadoProvider
    ) {
        this.contratacaoRepository = contratacaoRepository;
        this.servicoService = servicoService;
        this.usuarioService = usuarioService;
        this.usuarioAutenticadoProvider = usuarioAutenticadoProvider;
    }

    public Contratacao contratar(Long servicoId) {
        Servico servico = servicoService.buscarPorId(servicoId);

        if (!servico.estaAtivo()) {
            throw new RegraDeNegocioException("Esse serviço não está ativo e não pode ser contratado.");
        }

        Usuario contratante = usuarioService.buscarPorEmail(usuarioAutenticadoProvider.emailAtual());

        Contratacao contratacao = new Contratacao(servico, contratante);
        return contratacaoRepository.save(contratacao);
    }
}
