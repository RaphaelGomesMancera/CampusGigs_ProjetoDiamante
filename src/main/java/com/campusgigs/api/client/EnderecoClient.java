package com.campusgigs.api.client;

import org.springframework.web.service.annotation.GetExchange;

/**
 * EnderecoClient
 * -----------------------------------------------------------------------
 * Cliente HTTP declarativo (Spring 6 @HttpExchange) para o serviço
 * externo de CEP. Não tem nenhuma implementação aqui — o Spring gera um
 * proxy em tempo de execução (ver HttpClientConfig), então esta interface
 * só descreve o contrato do endpoint externo.
 * -----------------------------------------------------------------------
 */
public interface EnderecoClient {

    @GetExchange("/{cep}/json/")
    EnderecoViaCepResponse buscarPorCep(String cep);
}
