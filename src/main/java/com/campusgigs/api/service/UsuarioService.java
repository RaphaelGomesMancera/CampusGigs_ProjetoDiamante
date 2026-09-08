package com.campusgigs.api.service;

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

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cadastra um novo usuário como USER (ninguém vira ADMIN se auto
     * cadastrando — promoção a ADMIN é uma operação manual/administrativa,
     * fora do escopo deste endpoint).
     *
     * A resolução de cidade/UF a partir do `cep` acontece à parte, via
     * EnderecoService (CP5) — aqui só persistimos o CEP informado.
     */
    public Usuario registrar(RegistrarUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com esse e-mail.");
        }

        String senhaCriptografada = passwordEncoder.encode(request.senha());
        Usuario usuario = new Usuario(request.nome(), request.email(), senhaCriptografada, Papel.USER);
        usuario.setCep(request.cep());

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + email));
    }
}
