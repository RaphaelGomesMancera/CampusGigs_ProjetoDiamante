package com.campusgigs.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Contratacao
 * -----------------------------------------------------------------------
 * Um `contratante` contratando um `servico`. Regra de negócio central
 * (validada na camada de serviço, não aqui): o contratante nunca pode ser
 * o mesmo usuário que é o prestador do serviço.
 * -----------------------------------------------------------------------
 */
@Entity
@Table(name = "contratacoes")
public class Contratacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contratante_id", nullable = false)
    private Usuario contratante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoContratacao situacao = SituacaoContratacao.SOLICITADA;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    protected Contratacao() {
        // exigido pelo JPA
    }

    public Contratacao(Servico servico, Usuario contratante) {
        this.servico = servico;
        this.contratante = contratante;
    }

    // ---- getters e setters -------------------------------------------------

    public Long getId() {
        return id;
    }

    public Servico getServico() {
        return servico;
    }

    public Usuario getContratante() {
        return contratante;
    }

    public SituacaoContratacao getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoContratacao situacao) {
        this.situacao = situacao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
