-- =============================================================================
-- V1__schema_inicial.sql
-- -----------------------------------------------------------------------------
-- Schema inicial da CampusGigs (CP1).
--
-- Três tabelas centrais do domínio:
--   usuarios     -> aluno cadastrado (papel ADMIN ou USER)
--   servicos     -> freela publicado por um usuário (prestador)
--   contratacoes -> um usuário (contratante) contratando um serviço
--
-- Decisões de modelagem:
--   - papel/situacao são armazenados como VARCHAR (não enum nativo do Postgres)
--     para facilitar evoluir os valores em migrations futuras sem precisar de
--     "ALTER TYPE ... ADD VALUE".
--   - cidade/uf ficam nullable: são preenchidos pela integração com o serviço
--     de CEP (HttpExchange, CP5), então no cadastro inicial podem não existir
--     ainda caso a consulta externa falhe.
--   - preco usa NUMERIC(10,2) para evitar erros de arredondamento de float.
-- =============================================================================

CREATE TABLE usuarios (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(120)        NOT NULL,
    email           VARCHAR(160)        NOT NULL UNIQUE,
    senha_hash      VARCHAR(255)        NOT NULL,
    papel           VARCHAR(20)         NOT NULL DEFAULT 'USER',
    cep             VARCHAR(9),
    cidade          VARCHAR(120),
    uf              VARCHAR(2),
    criado_em       TIMESTAMP           NOT NULL DEFAULT now(),

    CONSTRAINT chk_usuarios_papel CHECK (papel IN ('ADMIN', 'USER'))
);

CREATE TABLE servicos (
    id              BIGSERIAL PRIMARY KEY,
    prestador_id    BIGINT              NOT NULL REFERENCES usuarios(id),
    titulo          VARCHAR(120)        NOT NULL,
    descricao       VARCHAR(2000)       NOT NULL,
    categoria       VARCHAR(60)         NOT NULL,
    preco           NUMERIC(10, 2)      NOT NULL CHECK (preco > 0),
    situacao        VARCHAR(20)         NOT NULL DEFAULT 'ATIVO',
    criado_em       TIMESTAMP           NOT NULL DEFAULT now(),

    CONSTRAINT chk_servicos_situacao CHECK (situacao IN ('ATIVO', 'PAUSADO', 'ENCERRADO'))
);

CREATE TABLE contratacoes (
    id              BIGSERIAL PRIMARY KEY,
    servico_id      BIGINT              NOT NULL REFERENCES servicos(id),
    contratante_id  BIGINT              NOT NULL REFERENCES usuarios(id),
    situacao        VARCHAR(20)         NOT NULL DEFAULT 'SOLICITADA',
    criado_em       TIMESTAMP           NOT NULL DEFAULT now(),

    CONSTRAINT chk_contratacoes_situacao
        CHECK (situacao IN ('SOLICITADA', 'ACEITA', 'CONCLUIDA', 'CANCELADA'))
);

-- Índices para as buscas mais comuns da API (listar serviços ativos,
-- listar contratações de um usuário).
CREATE INDEX idx_servicos_prestador   ON servicos (prestador_id);
CREATE INDEX idx_servicos_situacao    ON servicos (situacao);
CREATE INDEX idx_contratacoes_servico ON contratacoes (servico_id);
CREATE INDEX idx_contratacoes_contratante ON contratacoes (contratante_id);
