package com.campusgigs.api.client;

/**
 * Espelha o corpo retornado por https://viacep.com.br/ws/{cep}/json/.
 * Quando o CEP não existe, o ViaCEP responde 200 OK com {"erro": true} em
 * vez de 404 — por isso o campo `erro` existe aqui e é conferido
 * explicitamente em EnderecoService, em vez de confiarmos só no status HTTP.
 */
public record EnderecoViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        Boolean erro
) {

    public boolean encontrado() {
        return erro == null || !erro;
    }
}
