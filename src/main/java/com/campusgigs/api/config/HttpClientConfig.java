package com.campusgigs.api.config;

import com.campusgigs.api.client.EnderecoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

/**
 * HttpClientConfig
 * -----------------------------------------------------------------------
 * Monta o proxy do EnderecoClient em cima de um RestClient com timeout
 * curto — o enunciado pede explicitamente que a dupla pense no que
 * acontece quando o serviço externo "falha ou demora". Um timeout de 5s
 * evita que uma consulta de CEP trave a requisição de cadastro
 * indefinidamente; o EnderecoService trata o timeout como CEP não
 * resolvido (loga e segue sem cidade/UF), nunca deixa a exception subir
 * crua para o cliente da API.
 * -----------------------------------------------------------------------
 */
@Configuration
public class HttpClientConfig {

    private static final String VIA_CEP_BASE_URL = "https://viacep.com.br/ws";

    @Bean
    public EnderecoClient enderecoClient() {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        requestFactory.setReadTimeout((int) Duration.ofSeconds(5).toMillis());

        RestClient restClient = RestClient.builder()
                .baseUrl(VIA_CEP_BASE_URL)
                .requestFactory(requestFactory)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(EnderecoClient.class);
    }
}
