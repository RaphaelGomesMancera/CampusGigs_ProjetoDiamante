package com.campusgigs.api.service;

import com.campusgigs.api.dto.PublicarServicoRequest;
import com.campusgigs.api.exception.RecursoNaoEncontradoException;
import com.campusgigs.api.model.Servico;
import com.campusgigs.api.model.Usuario;
import com.campusgigs.api.repository.ServicoRepository;
import com.campusgigs.api.security.UsuarioAutenticadoProvider;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ServicoService (CP3)
 * -----------------------------------------------------------------------
 * Nesta etapa o foco é ter os endpoints funcionando atrás do JWT — ainda
 * sem diferenciar "dono do serviço" de "qualquer autenticado". Isso é
 * proposital: as regras de posse/papel (só o dono ou um ADMIN encerra)
 * entram no CP4, num commit separado, para deixar claro no histórico
 * quando a autorização por papel foi de fato aplicada.
 * -----------------------------------------------------------------------
 */
@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final UsuarioService usuarioService;
    private final UsuarioAutenticadoProvider usuarioAutenticadoProvider;

    public ServicoService(
            ServicoRepository servicoRepository,
            UsuarioService usuarioService,
            UsuarioAutenticadoProvider usuarioAutenticadoProvider
    ) {
        this.servicoRepository = servicoRepository;
        this.usuarioService = usuarioService;
        this.usuarioAutenticadoProvider = usuarioAutenticadoProvider;
    }

    public Servico publicar(PublicarServicoRequest request) {
        Usuario prestador = usuarioService.buscarPorEmail(usuarioAutenticadoProvider.emailAtual());

        Servico servico = new Servico(
                prestador,
                request.titulo(),
                request.descricao(),
                request.categoria(),
                request.preco()
        );

        return servicoRepository.save(servico);
    }

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado: id " + id));
    }

    public Servico encerrar(Long id) {
        Servico servico = buscarPorId(id);
        servico.encerrar();
        return servicoRepository.save(servico);
    }
}
