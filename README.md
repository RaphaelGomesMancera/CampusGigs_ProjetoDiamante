# CampusGigs — Projeto Diamante

API REST de uma plataforma de freelas entre alunos de uma universidade. Um
aluno se cadastra, publica um serviço (freela) e outro aluno, autenticado,
contrata esse serviço.

**Java Advanced — 2º semestre**

Integrantes:
- Raphael Gomes Mancera — RM562279
- Bruno Vinicius Barbosa — RM566366
- Guilherme de Andrade Martini — RM566087

## Stack

- Java 17 + Spring Boot 3
- Spring Security + JWT (autenticação/autorização por papel ADMIN/USER)
- Spring Data JPA + PostgreSQL
- Flyway (versionamento de schema)
- Docker / Docker Compose
- Cliente HTTP declarativo (`@HttpExchange`) para consultar CEP → cidade/UF

## Como rodar

Pré-requisito: Docker e Docker Compose instalados. Não é preciso instalar
Java, Maven ou Postgres na máquina.

```bash
docker-compose up --build
```

A API sobe em `http://localhost:8080`. O Postgres sobe junto, já com a
primeira migration (`V1__schema_inicial.sql`) aplicada automaticamente
pelo Flyway na inicialização da aplicação.

Para derrubar tudo (mantendo os dados do banco no volume):

```bash
docker-compose down
```

Para derrubar e apagar os dados do banco também:

```bash
docker-compose down -v
```

## Documentação interativa

Com a API no ar, a lista de endpoints fica disponível em:

```
http://localhost:8080/swagger-ui.html
```

## Exemplo de chamada autenticada

*(Endpoints de autenticação chegam no CP2 — este exemplo será atualizado
assim que `/auth/registrar` e `/auth/login` estiverem prontos.)*

```bash
# 1. Login para obter o token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "aluno@fiap.com.br", "senha": "senha123"}'

# 2. Usar o token retornado nas requisições seguintes
curl -X GET http://localhost:8080/servicos \
  -H "Authorization: Bearer <token>"
```

## Estrutura do projeto

```
src/main/java/com/campusgigs/api/
  model/        -> entidades JPA (Usuario, Servico, Contratacao) e enums
  repository/   -> Spring Data JPA repositories
  config/       -> configuração (Security, JWT, HttpExchange — a partir do CP2)
  exception/    -> exceptions de domínio e handler centralizado de erros
src/main/resources/
  application.yml
  db/migration/ -> migrations do Flyway (uma por checkpoint que mexe no schema)
```

## Checkpoints

| # | Entrega |
|---|---------|
| CP1 | Ambiente sobe via Docker; primeira migration com o schema inicial |
| CP2 | Cadastro e autenticação funcionando (senha protegida) |
| CP3 | Emissão e validação de token nos endpoints protegidos |
| CP4 | Regras de autorização por papel aplicadas |
| CP5 | Integração com o serviço externo de CEP e revisão final |

Cada checkpoint tem um commit com uma justificativa curta (1 a 3 linhas)
explicando uma decisão tomada naquele trecho.
