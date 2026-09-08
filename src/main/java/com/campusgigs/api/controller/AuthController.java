package com.campusgigs.api.controller;

import com.campusgigs.api.dto.LoginRequest;
import com.campusgigs.api.dto.RegistrarUsuarioRequest;
import com.campusgigs.api.dto.TokenResponse;
import com.campusgigs.api.dto.UsuarioResponse;
import com.campusgigs.api.model.Usuario;
import com.campusgigs.api.service.AutenticacaoService;
import com.campusgigs.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 * -----------------------------------------------------------------------
 * Único controller sem exigência de autenticação (ver SecurityConfig:
 * `/auth/**` é permitAll). Faz sentido: ninguém tem token antes de logar.
 * -----------------------------------------------------------------------
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AutenticacaoService autenticacaoService;

    public AuthController(UsuarioService usuarioService, AutenticacaoService autenticacaoService) {
        this.usuarioService = usuarioService;
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistrarUsuarioRequest request) {
        Usuario usuario = usuarioService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.de(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = autenticacaoService.autenticar(request);
        return ResponseEntity.ok(TokenResponse.bearer(token));
    }
}
