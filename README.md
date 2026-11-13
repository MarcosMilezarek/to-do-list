# To-Do List API

[![CI](https://github.com/MarcosMilezarek/to-do-list/actions/workflows/ci.yml/badge.svg)](https://github.com/MarcosMilezarek/to-do-list/actions/workflows/ci.yml)

> Projeto de estudo, feito para praticar back-end com Java e Spring. Não foi pensado para uso em produção.

API REST para gerenciamento de tarefas, com usuários e categorias, construída como projeto de estudo backend em Java.

## Stack

- Java 17
- Spring Boot 3.5.15 (Web, Data JPA, Validation, Security)
- MySQL 8
- Flyway (versionamento de schema)
- java-jwt (com.auth0) + BCrypt

## Como rodar

Pré-requisitos: JDK 17, MySQL rodando localmente.

1. Crie o banco:
   ```sql
   CREATE DATABASE taskdb;
   ```
2. Configure as variáveis de ambiente:
   - `DB_USERNAME` / `DB_PASSWORD`: credenciais do MySQL (default `root` / `admin` se omitidas).
   - `JWT_SECRET`: chave usada para assinar e validar os tokens. **Obrigatória, sem default:** sem ela a aplicação não sobe.
3. Rode a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

O Flyway aplica as migrações de `src/main/resources/db/migration` automaticamente na subida. A API sobe em `http://localhost:8080`.

## Autenticação

Autenticação stateless com JWT (Spring Security). Apenas os dois endpoints abaixo são públicos; todo o resto exige o header `Authorization: Bearer <token>`.

### Autenticação (`/auth`)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/register` | Cria um usuário (`name`, `email`, `senha`); a senha é armazenada com BCrypt. Devolve o usuário criado (`id`, `nome`), **sem token** |
| POST | `/auth/login` | Autentica (`login` = email, `senha`) e devolve `{ token }` |

Registrar não autentica sozinho: para obter um token, chame `POST /auth/login` logo em seguida com a mesma senha.

Falha de autenticação (email inexistente, senha errada, token ausente/inválido/expirado) sempre responde **401**, sem distinguir o motivo.

### Tarefa (`/tasks`)

Todas as rotas abaixo exigem token e são sempre relativas ao usuário autenticado: o id do dono vem do token, nunca da URL. Uma tarefa de outro usuário responde **404** (não 403), para não revelar que o id existe.

| Método | Rota | Descrição |
|---|---|---|
| POST | `/tasks` | Cria uma tarefa para o usuário autenticado |
| GET | `/tasks/{id}` | Busca uma tarefa do usuário autenticado por id |
| PUT | `/tasks/{id}` | Atualiza uma tarefa do usuário autenticado |
| DELETE | `/tasks/{id}` | Remove uma tarefa do usuário autenticado (204 sem corpo) |
| GET | `/tasks` | Lista as tarefas do usuário autenticado, paginado e com filtros opcionais |

Filtros e paginação de `GET /tasks` são combináveis via query params:

- `status` (opcional)
- `categoriaId` (opcional)
- `page`, `size`, `sort` (paginação padrão do Spring Data; default `sort=id,asc`)

Categorias ainda não têm endpoint próprio; a tabela `category` existe no schema mas é populada manualmente.

## Tratamento de erros

Erros de validação e regras de negócio retornam um corpo JSON padronizado (`path`, `message`, `time`, `status`), com um handler central (`GlobalExceptionHandler`) cobrindo:

- Validação de campos (`@Valid`) → 400
- Tarefa não encontrada (ou de outro usuário) → 404
- Usuário não encontrado → 404
- Falha de autenticação → 401
- Método HTTP não suportado na rota → 405
- Rota inexistente → 404

## Documentação da API

Com a aplicação rodando, a documentação interativa fica em `http://localhost:8080/swagger-ui.html` e a especificação OpenAPI em `http://localhost:8080/v3/api-docs`. As duas são públicas.

Para chamar as rotas protegidas pelo Swagger:

1. Crie um usuário em `POST /auth/register`.
2. Faça login em `POST /auth/login` e copie o valor de `token`.
3. Clique em **Authorize** e cole o token, sem o prefixo `Bearer`.

Como alternativa, a pasta `postman/` tem uma coleção para o Postman. Ela ainda usa rotas antigas (`POST /user` e `POST /tasks/user/{id}`) e não envia token, então precisa ser ajustada antes do uso.

## Testes

```bash
./mvnw test
```

Os testes usam o perfil `test` (`src/test/resources/application-test.properties`), com H2 em memória no modo MySQL. As migrações do Flyway rodam no H2 e o Hibernate valida o schema, então não é preciso ter MySQL nem definir variáveis de ambiente. O GitHub Actions roda o mesmo comando a cada push e pull request para a `main`.

| Classe | Tipo | O que cobre |
|---|---|---|
| `TaskRepositoryTest` | `@DataJpaTest` | Consulta paginada traz só as tarefas do usuário consultado (cenário com dois usuários), filtros por status e categoria, tamanho da página e busca por id restrita ao dono |
| `TaskControllerTest` | `@WebMvcTest` + MockMvc | Rotas de `/tasks`: sucesso, validação (400), JSON mal formado (400), tarefa inexistente (404), sem token ou com token inválido (401) |
| `UserControllerTest` | `@WebMvcTest` + MockMvc | Registro e login: sucesso, validação (400), credenciais inválidas (401), rota inexistente (404) e rota protegida sem token (401) |
| `TaskServiceImplTest` | Mockito | Consulta paginada convertida em DTO |
| `UserServiceImplTest` | Mockito | Criação de usuário com senha codificada e busca por email com e sem resultado |
| `OpenApiConfigTest` | `@SpringBootTest` + MockMvc | Swagger e `/v3/api-docs` acessíveis sem token; as demais rotas continuam protegidas |
| `TodolistApplicationTests` | `@SpringBootTest` | O contexto completo da aplicação sobe |

## Decisões em aberto

- Mapeamento entidade↔DTO é manual (MapStruct foi avaliado e descartado por ora, por não se justificar no tamanho atual do projeto).
- `Usuario.email` ainda não tem constraint `unique`: `POST /auth/register` não deduplica por email.
