package com.campusgigs.api.dto;

import com.campusgigs.api.model.Papel;
import com.campusgigs.api.model.Usuario;

/**
 * Nunca devolvemos `senhaHash` no corpo da resposta — esse DTO existe
 * justamente para garantir que a entidade Usuario nunca "vaze" inteira
 * pela API.
 */
public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        Papel papel,
        String cidade,
        String uf
) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPapel(),
                usuario.getCidade(),
                usuario.getUf()
        );
    }
}
