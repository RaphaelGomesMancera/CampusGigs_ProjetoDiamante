package com.campusgigs.api.service;

import com.campusgigs.api.dto.PublicarServicoRequest;
import com.campusgigs.api.exception.AcessoNegadoException;
import com.campusgigs.api.exception.RecursoNaoEncontradoException;
import com.campusgigs.api.model.Papel;
import com.campusgigs.api.model.Servico;
import com.campusgigs.api.model.Usuario;
import com.campusgigs.api.repository.ServicoRepository;
import com.campusgigs.api.security.UsuarioAutenticadoProvider;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ServicoService (CP4)
 * -----------------------------------------------------------------------
 * `encerrar` agora aplica a regra do enunciado: "um usuário só edita ou
 * encerra os próprios serviços — exceto um ADMIN, que pode encerrar
 * qualquer um". A checagem fica no service (não em @PreAuthorize) porque
 * depende do dado da linha (quem é o prestador daquele serviço
 * específico), não só do papel — e assim a mensagem de erro fica
 * específica em vez do 403 genérico do Spring Security.
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

    /** Mesma regra de posse do `encerrar`: só o dono ou um ADMIN edita. */
    public Servico editar(Long id, PublicarServicoRequest request) {
        Servico servico = buscarPorId(id);
        Usuario usuarioAtual = usuarioService.buscarPorEmail(usuarioAutenticadoProvider.emailAtual());

        if (!servico.pertenceA(usuarioAtual) && usuarioAtual.getPapel() != Papel.ADMIN) {
            throw new AcessoNegadoException("Você só pode editar os seus próprios serviços.");
        }

        servico.setTitulo(request.titulo());
        servico.setDescricao(request.descricao());
        servico.setCategoria(request.categoria());
        servico.setPreco(request.preco());

        return servicoRepository.save(servico);
    }

    public Servico encerrar(Long id) {
        Servico servico = buscarPorId(id);
        Usuario usuarioAtual = usuarioService.buscarPorEmail(usuarioAutenticadoProvider.emailAtual());

        boolean ehDono = servico.pertenceA(usuarioAtual);
        boolean ehAdmin = usuarioAtual.getPapel() == Papel.ADMIN;

        if (!ehDono && !ehAdmin) {
            throw new AcessoNegadoException("Você só pode encerrar os seus próprios serviços.");
        }

        servico.encerrar();
        return servicoRepository.save(servico);
    }
}
