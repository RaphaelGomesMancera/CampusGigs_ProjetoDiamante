package com.campusgigs.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JwtAuthFilter
 * -----------------------------------------------------------------------
 * Roda uma vez por requisição, antes do filtro padrão de autenticação do
 * Spring. Lê o header `Authorization: Bearer <token>`; se o token for
 * válido, popula o SecurityContext com o e-mail e o papel (como
 * ROLE_ADMIN/ROLE_USER) extraídos do próprio token — sem nenhuma consulta
 * ao banco a cada requisição.
 *
 * Se não houver token, ou ele for inválido/expirado, o filtro simplesmente
 * segue a cadeia sem autenticar; quem decide se isso é um problema é a
 * regra de autorização do endpoint (authenticated() barra com 401/403 nos
 * casos configurados no SecurityConfig).
 * -----------------------------------------------------------------------
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String PREFIXO_BEARER = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith(PREFIXO_BEARER)) {
            String token = header.substring(PREFIXO_BEARER.length());

            if (jwtService.tokenValido(token)) {
                String email = jwtService.extrairEmail(token);
                String papel = jwtService.extrairPapel(token);

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + papel));
                var authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // Token presente mas inválido/expirado: limpa o contexto para o
                // AuthenticationEntryPoint responder 401 (não 403).
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
