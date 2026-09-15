package com.campusgigs.api.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

/**
 * EnderecoClient
 * -----------------------------------------------------------------------
 * Cliente HTTP declarativo (Spring 6 @HttpExchange) para o serviço
 * externo de CEP. Não tem nenhuma implementação aqui — o Spring gera um
 * proxy em tempo de execução (ver HttpClientConfig), então esta interface
 * só descreve o contrato do endpoint externo.
 *
 * O @PathVariable("cep") é obrigatório: sem ele o placeholder {cep} da
 * URL não é preenchido e a consulta ao ViaCEP falha.
 * -----------------------------------------------------------------------
 */
public interface EnderecoClient {

    @GetExchange("/{cep}/json/")
    EnderecoViaCepResponse buscarPorCep(@PathVariable("cep") String cep);
}
