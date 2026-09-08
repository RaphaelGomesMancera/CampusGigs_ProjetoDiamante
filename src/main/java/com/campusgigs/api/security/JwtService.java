package com.campusgigs.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

/**
 * JwtService
 * -----------------------------------------------------------------------
 * Responsável só por gerar e ler tokens JWT — não sabe nada de HTTP ou de
 * Spring Security (isso fica no JwtAuthFilter, CP3). Guardamos o e-mail do
 * usuário como subject e o papel (ADMIN/USER) como claim customizada, para
 * o filtro conseguir montar a autoridade sem precisar consultar o banco a
 * cada requisição.
 * -----------------------------------------------------------------------
 */
@Component
public class JwtService {

    private final SecretKey chave;
    private final long expiracaoMinutos;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiracao-minutos}") long expiracaoMinutos
    ) {
        this.chave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracaoMinutos = expiracaoMinutos;
    }

    public String gerarToken(String email, String papel) {
        Instant agora = Instant.now();

        return Jwts.builder()
                .subject(email)
                .claim("papel", papel)
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plus(expiracaoMinutos, ChronoUnit.MINUTES)))
                .signWith(chave)
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    public String extrairPapel(String token) {
        return extrairClaim(token, claims -> claims.get("papel", String.class));
    }

    /**
     * Valida assinatura e expiração. Retorna false em vez de deixar a
     * exception do jjwt vazar — quem chama só precisa saber "válido ou não".
     */
    public boolean tokenValido(String token) {
        try {
            extrairTodasClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private <T> T extrairClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extrairTodasClaims(token));
    }

    private Claims extrairTodasClaims(String token) {
        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
