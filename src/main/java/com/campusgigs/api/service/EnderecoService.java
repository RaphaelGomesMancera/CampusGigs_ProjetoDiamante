package com.campusgigs.api.service;

import com.campusgigs.api.client.EnderecoClient;
import com.campusgigs.api.client.EnderecoViaCepResponse;
import com.campusgigs.api.exception.RegraDeNegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

/**
 * EnderecoService
 * -----------------------------------------------------------------------
 * Isola a regra de negócio em cima do EnderecoClient (HttpExchange, CP5).
 *
 * Duas falhas possíveis, tratadas de forma diferente:
 *   - CEP com formato válido mas que não existe -> RegraDeNegocioException
 *     (400/409 claro para quem chamou a API, "operação não fica
 *     silenciosamente incompleta", como pede o enunciado).
 *   - Serviço externo fora do ar ou demorando (timeout) -> não derruba o
 *     cadastro do usuário; loga o problema e devolve um Resultado vazio,
 *     deixando cidade/UF em branco para serem preenchidos depois.
 * -----------------------------------------------------------------------
 */
@Service
public class EnderecoService {

    private static final Logger log = LoggerFactory.getLogger(EnderecoService.class);

    private final EnderecoClient enderecoClient;

    public EnderecoService(EnderecoClient enderecoClient) {
        this.enderecoClient = enderecoClient;
    }

    /**
     * @return o endereço resolvido, ou null se o serviço externo estava
     *         indisponível/lento (falha "silenciosa" e intencional — ver
     *         classe acima). Se o CEP simplesmente não existir, lança
     *         RegraDeNegocioException em vez de retornar null, porque
     *         nesse caso o problema é do dado enviado pelo usuário, não
     *         do serviço externo.
     */
    public EnderecoViaCepResponse resolver(String cep) {
        try {
            EnderecoViaCepResponse resposta = enderecoClient.buscarPorCep(cep);

            if (resposta == null || !resposta.encontrado()) {
                throw new RegraDeNegocioException("CEP não encontrado: " + cep + ".");
            }

            return resposta;
        } catch (RestClientException ex) {
            log.warn("Falha ao consultar o serviço de CEP para '{}': {}", cep, ex.getMessage());
            return null;
        }
    }
}
