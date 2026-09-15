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

A API sobe em `http://localhost:8080`. O Postgres sobe junto, com as
migrations Flyway aplicadas automaticamente (`V1` schema + `V2` admin demo).

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

## Usuário ADMIN de demonstração

Criado automaticamente pela migration `V2__usuario_admin_demo.sql`:

| Campo | Valor |
|---|---|
| E-mail | `admin@campusgigs.local` |
| Senha | `senha123` |
| Papel | `ADMIN` |

Use esse usuário para provar que um ADMIN pode encerrar o serviço de outro aluno.

## Endpoints

| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| POST | `/auth/registrar` | pública | Cadastra usuário (papel USER); resolve cidade/UF pelo CEP, se informado |
| POST | `/auth/login` | pública | Autentica e devolve o token JWT |
| GET | `/usuarios/me` | qualquer autenticado | Perfil do usuário do token |
| PATCH | `/usuarios/me/cep` | qualquer autenticado | Atualiza o CEP e re-resolve cidade/UF |
| POST | `/servicos` | qualquer autenticado | Publica um serviço (freela) |
| GET | `/servicos` | qualquer autenticado | Lista todos os serviços |
| GET | `/servicos/{id}` | qualquer autenticado | Busca um serviço por id |
| PUT | `/servicos/{id}` | dono do serviço ou ADMIN | Edita título/descrição/categoria/preço |
| PATCH | `/servicos/{id}/encerrar` | dono do serviço ou ADMIN | Encerra o serviço |
| POST | `/servicos/{id}/contratar` | qualquer autenticado, exceto o dono | Contrata um serviço ATIVO |

## Exemplo de chamada autenticada

```bash
# 1. Cadastrar um usuário (o CEP é resolvido para cidade/UF automaticamente)
curl -X POST http://localhost:8080/auth/registrar \
  -H "Content-Type: application/json" \
  -d "{\"nome\": \"Ana Souza\", \"email\": \"ana@fiap.com.br\", \"senha\": \"senha123\", \"cep\": \"01001000\"}"

# 2. Login para obter o token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\": \"ana@fiap.com.br\", \"senha\": \"senha123\"}"
# -> { "tokenAcesso": "eyJhbGciOi...", "tipo": "Bearer" }

# 3. Usar o token nas requisições seguintes
curl -X POST http://localhost:8080/servicos \
  -H "Authorization: Bearer eyJhbGciOi..." \
  -H "Content-Type: application/json" \
  -d "{\"titulo\": \"Aulas de Calculo 1\", \"descricao\": \"Reforco para provas e listas.\", \"categoria\": \"AULA_PARTICULAR\", \"preco\": 50.00}"
```

## Evidência de testes manuais

### 1) Sem token → 401 Unauthorized

```bash
curl -i -X GET http://localhost:8080/servicos
# HTTP/1.1 401
# {"status":401,"erro":"Não autenticado","mensagem":"Token ausente, inválido ou expirado."}
```

### 2) Acesso negado por papel/posse → 403 Forbidden

Cadastre um segundo usuário (Bruno), faça login e tente encerrar o serviço da Ana:

```bash
curl -i -X PATCH http://localhost:8080/servicos/1/encerrar \
  -H "Authorization: Bearer <token-do-bruno>"
# HTTP/1.1 403
# {"status":403,"erro":"Acesso negado","mensagem":"Você só pode encerrar os seus próprios serviços."}
```

### 3) ADMIN encerra serviço de outro → 200 OK

```bash
# Login do admin (seed V2)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\": \"admin@campusgigs.local\", \"senha\": \"senha123\"}"

curl -i -X PATCH http://localhost:8080/servicos/1/encerrar \
  -H "Authorization: Bearer <token-do-admin>"
# HTTP/1.1 200 — serviço encerrado
```

Também é possível repetir esses cenários pelo Swagger UI em
`http://localhost:8080/swagger-ui.html` (Authorize com o Bearer token).

## Estrutura do projeto

```
src/main/java/com/campusgigs/api/
  model/        -> entidades JPA (Usuario, Servico, Contratacao) e enums
  repository/   -> Spring Data JPA repositories
  security/     -> JWT, filtro, 401/403 em JSON
  config/       -> HttpExchange (ViaCEP)
  exception/    -> exceptions de domínio e handler centralizado de erros
src/main/resources/
  application.yml
  db/migration/ -> V1 schema inicial; V2 admin demo
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
