# To-Do List API

API REST para gerenciamento de tarefas, com usuários e categorias, construída como projeto de estudo backend em Java.

## Stack

- Java 17
- Spring Boot 3.5.15 (Web, Data JPA, Validation)
- MySQL 8
- Flyway (versionamento de schema)
- Lombok

## Como rodar

Pré-requisitos: JDK 17, MySQL rodando localmente.

1. Crie o banco:
   ```sql
   CREATE DATABASE taskdb;
   ```
2. Configure usuário/senha do banco em `src/main/resources/application.properties` (`spring.datasource.username` / `spring.datasource.password`).
3. Rode a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

O Flyway aplica as migrações de `src/main/resources/db/migration` automaticamente na subida. A API sobe em `http://localhost:8080`.

## Endpoints

### Usuário (`/user`)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/user` | Cria um usuário (`name`, `email`) |
| GET | `/user/login/{email}` | Busca usuário por email |

### Tarefa (`/tasks`)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/tasks/user/{id_user}` | Cria uma tarefa para o usuário informado |
| GET | `/tasks/{id}` | Busca uma tarefa por id |
| PUT | `/tasks/{id}` | Atualiza uma tarefa |
| DELETE | `/tasks/{id}` | Remove uma tarefa |
| GET | `/tasks/user/{id_user}` | Lista as tarefas do usuário, paginado e com filtros opcionais |

Filtros e paginação de `GET /tasks/user/{id_user}` são combináveis via query params:

- `status` (opcional)
- `categoriaId` (opcional)
- `page`, `size`, `sort` (paginação padrão do Spring Data)

Categorias ainda não têm endpoint próprio; a tabela `category` existe no schema mas é populada manualmente.

## Tratamento de erros

Erros de validação e regras de negócio retornam um corpo JSON padronizado (`path`, `message`, `time`, `status`), com um handler central (`GlobalExceptionHandler`) cobrindo:

- Validação de campos (`@Valid`) → 400
- Tarefa não encontrada → 404
- Usuário não encontrado → 404
- Método HTTP não suportado na rota → 405
- Rota inexistente → 404

## Decisões em aberto

- `GET /tasks/user/{id}` para um usuário inexistente retorna 200 com página vazia, em vez de 404. Decisão de produto ainda não fechada.
- Mapeamento entidade↔DTO é manual (MapStruct foi avaliado e descartado por ora, por não se justificar no tamanho atual do projeto).
