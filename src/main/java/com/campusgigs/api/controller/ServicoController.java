package com.campusgigs.api.controller;

import com.campusgigs.api.dto.ContratacaoResponse;
import com.campusgigs.api.dto.PublicarServicoRequest;
import com.campusgigs.api.dto.ServicoResponse;
import com.campusgigs.api.model.Servico;
import com.campusgigs.api.service.ContratacaoService;
import com.campusgigs.api.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ServicoController
 * -----------------------------------------------------------------------
 * Todos os endpoints exigem token válido (ver SecurityConfig: "/servicos/**"
 * está em authenticated()). Sem header Authorization, ou com token
 * inválido/expirado, o Spring Security já barra com 401 antes de chegar
 * aqui — nenhuma lógica de autenticação precisa ser repetida no controller.
 * -----------------------------------------------------------------------
 */
@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;
    private final ContratacaoService contratacaoService;

    public ServicoController(ServicoService servicoService, ContratacaoService contratacaoService) {
        this.servicoService = servicoService;
        this.contratacaoService = contratacaoService;
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> publicar(@Valid @RequestBody PublicarServicoRequest request) {
        Servico servico = servicoService.publicar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ServicoResponse.de(servico));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> listar() {
        List<ServicoResponse> resposta = servicoService.listarTodos().stream()
                .map(ServicoResponse::de)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ServicoResponse.de(servicoService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> editar(@PathVariable Long id, @Valid @RequestBody PublicarServicoRequest request) {
        return ResponseEntity.ok(ServicoResponse.de(servicoService.editar(id, request)));
    }

    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<ServicoResponse> encerrar(@PathVariable Long id) {
        return ResponseEntity.ok(ServicoResponse.de(servicoService.encerrar(id)));
    }

    @PostMapping("/{id}/contratar")
    public ResponseEntity<ContratacaoResponse> contratar(@PathVariable Long id) {
        var contratacao = contratacaoService.contratar(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(ContratacaoResponse.de(contratacao));
    }
}
