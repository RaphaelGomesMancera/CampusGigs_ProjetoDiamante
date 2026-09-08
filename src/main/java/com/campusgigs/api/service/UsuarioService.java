package com.campusgigs.api.service;

import com.campusgigs.api.client.EnderecoViaCepResponse;
import com.campusgigs.api.dto.RegistrarUsuarioRequest;
import com.campusgigs.api.exception.RecursoNaoEncontradoException;
import com.campusgigs.api.exception.RegraDeNegocioException;
import com.campusgigs.api.model.Papel;
import com.campusgigs.api.model.Usuario;
import com.campusgigs.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EnderecoService enderecoService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EnderecoService enderecoService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.enderecoService = enderecoService;
    }

    /**
     * Cadastra um novo usuário como USER (ninguém vira ADMIN se auto
     * cadastrando — promoção a ADMIN é uma operação manual/administrativa,
     * fora do escopo deste endpoint).
     */
    public Usuario registrar(RegistrarUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com esse e-mail.");
        }

        String senhaCriptografada = passwordEncoder.encode(request.senha());
        Usuario usuario = new Usuario(request.nome(), request.email(), senhaCriptografada, Papel.USER);

        if (request.cep() != null && !request.cep().isBlank()) {
            aplicarEndereco(usuario, request.cep());
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Atualiza o CEP de um usuário já cadastrado, resolvendo cidade/UF de
     * novo via HttpExchange (CP5) — mesmo fluxo usado no cadastro.
     */
    public Usuario atualizarCep(String email, String cep) {
        Usuario usuario = buscarPorEmail(email);
        aplicarEndereco(usuario, cep);
        return usuarioRepository.save(usuario);
    }

    /** Se o CEP não existir, RegraDeNegocioException interrompe a operação
     *  (nunca fica "silenciosamente incompleta", como pede o enunciado). Se
     *  o serviço externo estiver fora do ar, seguimos com cidade/UF em
     *  branco — ver EnderecoService. */
    private void aplicarEndereco(Usuario usuario, String cep) {
        usuario.setCep(cep);

        EnderecoViaCepResponse endereco = enderecoService.resolver(cep);
        if (endereco != null) {
            usuario.setCidade(endereco.localidade());
            usuario.setUf(endereco.uf());
        }
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + email));
    }
}
