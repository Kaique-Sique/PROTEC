<img width="591" height="396" alt="image" src="https://github.com/user-attachments/assets/7cb790ca-3796-400f-8a36-467538524d1b" />

# PROTEC API

API do servidor central do PROTEC, sistema de monitoramento de EPIs e segurança industrial. O sistema usa visão computacional para identificar ausência ou uso inadequado de EPIs; as ocorrências devem ser registradas e consultadas por esta plataforma centralizada.

Este repositório cobre, por enquanto, o **cadastro de máquinas** (`maquinas`): cada máquina tem um nome e os riscos da atividade.

## Stack

- Java 17, Spring Boot 4.1.0, Gradle (wrapper incluído)
- Spring Web + Bean Validation
- Spring Data JPA (máquinas) e `JdbcTemplate` (health check)
- PostgreSQL (Supabase); H2 em memória nos testes
- springdoc-openapi 3.x (Swagger UI)

## Configuração

Copie `.env.example` para `.env` (nunca faça commit do `.env`) e preencha:

| Variável      | Descrição                         |
| ------------- | --------------------------------- |
| `DB_HOST`     | Host do banco                     |
| `DB_PORT`     | Porta (padrão `5432`)             |
| `DB_NAME`     | Nome do banco                     |
| `DB_USER`     | Usuário                           |
| `DB_PASSWORD` | Senha                             |

A tabela `maquinas` é criada (se não existir) pelo `src/main/resources/schema.sql` ao subir a aplicação, e o Hibernate apenas a valida (`ddl-auto: validate`). Se o banco já tiver uma tabela `maquinas` com estrutura diferente, ajuste-a ou troque temporariamente para `ddl-auto: none`.

## Como rodar

```bash
./gradlew bootRun    # sobe em http://localhost:8081
./gradlew build      # compila e roda os testes (usam H2, não precisam de banco real)
```

## Endpoints

| Método   | Caminho                    | Descrição                                       | Respostas              |
| -------- | -------------------------- | ----------------------------------------------- | ---------------------- |
| `GET`    | `/health`                  | Status do serviço e do banco                    | 200, 503 se banco cair |
| `GET`    | `/actuator/health`         | Health do Spring Boot Actuator                  | 200                    |
| `GET`    | `/maquinas`                | Lista todas as máquinas                         | 200                    |
| `GET`    | `/maquinas/{id}`           | Busca por UUID                                  | 200, 400, 404          |
| `GET`    | `/maquinas/nome/{nome}`    | Busca por nome                                  | 200, 404               |
| `POST`   | `/maquinas`                | Cria máquina (`nome`, `riscos`)                 | 201 + `Location`, 400, 409 |
| `PUT`    | `/maquinas/{id}`           | Substitui `nome` e `riscos`                     | 200, 400, 404, 409     |
| `PATCH`  | `/maquinas/{id}/nome`      | Altera só o nome                                | 200, 400, 404, 409     |
| `PATCH`  | `/maquinas/{id}/riscos`    | Altera só os riscos                             | 200, 400, 404          |
| `DELETE` | `/maquinas/{id}`           | Remove a máquina                                | 204, 404               |

Exemplo:

```bash
curl -X POST http://localhost:8081/maquinas \
  -H "Content-Type: application/json" \
  -d '{"nome":"Prensa hidráulica","riscos":"Esmagamento de mãos; ruído"}'
```

Regras: `nome` (obrigatório, até 50 caracteres, único) e `riscos` (obrigatório, até 255). Os erros seguem o formato ProblemDetail (RFC 7807).

Documentação interativa: Swagger UI em `/docs` e OpenAPI JSON em `/api-docs`.

## Estrutura

```
src/main/java/com/example/protec/
├── ProtecApplication.java
├── config/      OpenAPI e tratamento global de erros
├── health/      endpoint /health (checagem via JdbcTemplate)
└── maquinas/    entidade, repository, service, controller, MaquinasRequests (DTOs) e MaquinasErrors (exceções)
src/main/resources/
├── application.yaml
└── schema.sql
```

## Roadmap

Ainda não implementado:

- EPIs obrigatórios por máquina e seus CAs (Certificado de Aprovação)
- Registro de ocorrências
- Autenticação/autorização (hoje todos os endpoints, inclusive `DELETE`, são abertos)
- Migrations versionadas (Flyway/Liquibase) no lugar do `schema.sql`
