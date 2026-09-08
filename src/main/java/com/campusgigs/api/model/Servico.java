package com.campusgigs.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Servico (freela)
 * -----------------------------------------------------------------------
 * Publicado por um `prestador`. Regras aplicadas na camada de serviço
 * (não aqui — a entidade só guarda estado):
 *   - só o próprio prestador (ou um ADMIN) pode editar/encerrar (CP4)
 *   - só é contratável enquanto `situacao == ATIVO`
 * -----------------------------------------------------------------------
 */
@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prestador_id", nullable = false)
    private Usuario prestador;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(nullable = false, length = 60)
    private String categoria;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoServico situacao = SituacaoServico.ATIVO;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    protected Servico() {
        // exigido pelo JPA
    }

    public Servico(Usuario prestador, String titulo, String descricao, String categoria, BigDecimal preco) {
        this.prestador = prestador;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.preco = preco;
    }

    // ---- comportamento de domínio -------------------------------------------

    public boolean pertenceA(Usuario usuario) {
        return this.prestador.getId().equals(usuario.getId());
    }

    public boolean estaAtivo() {
        return this.situacao == SituacaoServico.ATIVO;
    }

    public void encerrar() {
        this.situacao = SituacaoServico.ENCERRADO;
    }

    // ---- getters e setters -------------------------------------------------

    public Long getId() {
        return id;
    }

    public Usuario getPrestador() {
        return prestador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public SituacaoServico getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoServico situacao) {
        this.situacao = situacao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
