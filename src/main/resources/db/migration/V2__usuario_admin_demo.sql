-- =============================================================================
-- V2__usuario_admin_demo.sql
-- -----------------------------------------------------------------------------
-- Usuário ADMIN de demonstração para validar a regra do enunciado:
-- "um ADMIN pode encerrar qualquer serviço".
--
-- Credenciais (somente ambiente acadêmico / local):
--   e-mail : admin@campusgigs.local
--   senha  : senha123
--
-- A senha está em BCrypt ($2b$ / compatível com BCryptPasswordEncoder).
-- ON CONFLICT evita erro se a migration for reaplicada em banco já seedado.
-- =============================================================================

INSERT INTO usuarios (nome, email, senha_hash, papel, criado_em)
VALUES (
    'Admin CampusGigs',
    'admin@campusgigs.local',
    '$2a$10$hwjAmHwpXcDzLPw4aT.F3uQV9/ccqCC4XneFtORn6jmux50rkx31y',
    'ADMIN',
    now()
)
ON CONFLICT (email) DO NOTHING;
