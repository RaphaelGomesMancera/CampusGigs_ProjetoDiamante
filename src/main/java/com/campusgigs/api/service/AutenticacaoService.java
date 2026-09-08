package com.campusgigs.api.service;

import com.campusgigs.api.dto.LoginRequest;
import com.campusgigs.api.model.Usuario;
import com.campusgigs.api.repository.UsuarioRepository;
import com.campusgigs.api.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AutenticacaoService
 * -----------------------------------------------------------------------
 * Confere e-mail/senha e emite o token JWT. De propósito não usa o
 * AuthenticationManager padrão do Spring Security aqui — com só um fluxo
 * de login (e-mail + senha), fazer a checagem direta com o
 * PasswordEncoder é mais simples e igualmente seguro.
 * -----------------------------------------------------------------------
 */
@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AutenticacaoService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String autenticar(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                // Mensagem idêntica à de senha errada: não revelamos se o
                // e-mail existe ou não.
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha incorretos."));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new BadCredentialsException("E-mail ou senha incorretos.");
        }

        return jwtService.gerarToken(usuario.getEmail(), usuario.getPapel().name());
    }
}
