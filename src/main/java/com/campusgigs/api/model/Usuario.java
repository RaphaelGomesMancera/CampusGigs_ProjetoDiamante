package com.campusgigs.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Usuario
 * -----------------------------------------------------------------------
 * Aluno cadastrado na plataforma. A senha nunca é exposta em texto puro —
 * `senhaHash` guarda o resultado do BCrypt (ver SecurityConfig, CP2).
 *
 * `cidade`/`uf` ficam nulos até a integração com o serviço de CEP
 * (HttpExchange, CP5) resolver o endereço a partir do `cep`.
 * -----------------------------------------------------------------------
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Papel papel = Papel.USER;

    @Column(length = 9)
    private String cep;

    @Column(length = 120)
    private String cidade;

    @Column(length = 2)
    private String uf;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    protected Usuario() {
        // exigido pelo JPA
    }

    public Usuario(String nome, String email, String senhaHash, Papel papel) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.papel = papel;
    }

    // ---- getters e setters -------------------------------------------------

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
