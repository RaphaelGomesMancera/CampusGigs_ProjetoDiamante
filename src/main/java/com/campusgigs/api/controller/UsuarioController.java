package com.campusgigs.api.controller;

import com.campusgigs.api.dto.AtualizarCepRequest;
import com.campusgigs.api.dto.UsuarioResponse;
import com.campusgigs.api.security.UsuarioAutenticadoProvider;
import com.campusgigs.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UsuarioController
 * -----------------------------------------------------------------------
 * Endpoints do próprio usuário autenticado — sempre agindo sobre "quem é
 * o dono do token", nunca recebendo um id de usuário por parâmetro (o que
 * abriria brecha para um usuário alterar o cadastro de outro).
 * -----------------------------------------------------------------------
 */
@RestController
@RequestMapping("/usuarios/me")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioAutenticadoProvider usuarioAutenticadoProvider;

    public UsuarioController(UsuarioService usuarioService, UsuarioAutenticadoProvider usuarioAutenticadoProvider) {
        this.usuarioService = usuarioService;
        this.usuarioAutenticadoProvider = usuarioAutenticadoProvider;
    }

    @GetMapping
    public ResponseEntity<UsuarioResponse> meuPerfil() {
        var usuario = usuarioService.buscarPorEmail(usuarioAutenticadoProvider.emailAtual());
        return ResponseEntity.ok(UsuarioResponse.de(usuario));
    }

    /** Consulta o CEP no serviço externo (CP5) e atualiza cidade/UF. */
    @PatchMapping("/cep")
    public ResponseEntity<UsuarioResponse> atualizarCep(@Valid @RequestBody AtualizarCepRequest request) {
        var usuario = usuarioService.atualizarCep(usuarioAutenticadoProvider.emailAtual(), request.cep());
        return ResponseEntity.ok(UsuarioResponse.de(usuario));
    }
}
