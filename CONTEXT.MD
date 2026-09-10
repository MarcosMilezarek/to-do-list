# CONTEXT.md — Java Backend Junior

Arquivo de contexto do projeto. Lido pelo orientador no início de cada sessão.
Atualizado manualmente pelo desenvolvedor ao concluir etapas.

---

## Estado atual

**Fase**: Fase 1 - API completa com Spring Boot + JPA. Trilha de frontend em React entra em paralelo a partir da Semana 4.
**Semana em andamento**: Semana 4

**Trilhas e agentes**:
- **Backend** (agente próprio): construção da API. Semanas 1-3 já entregaram CRUD de Task, tratamento de erro centralizado e relacionamentos JPA.
- **Frontend** (agente próprio): app React que só consome os serviços do backend. Sem regra de negócio no front. Começa na Semana 4.

**Objetivo da trilha frontend**: MVP do to-do list, fechado ao fim da Fase 1 (Semana 6), funcionando end-to-end contra a API.

**Regra de sincronização**: toda entrega do backend tem entrega correspondente no frontend na mesma semana ou na seguinte. O que o backend construiu antes da Semana 4 (CRUD, contrato de erro) passa a ser consumido pelo frontend a partir da Semana 4.

**Próximo desafio backend**: DTOs de request e response, MapStruct, Flyway.
**Próximo desafio frontend**: setup do projeto React e camada de serviço consumindo a API.

**Local do código frontend**: a definir pelo agente de frontend (pasta no mesmo repositório ou repositório separado).

---

## Histórico

### Tarefa inicial - Concluída
Correções pontuais no código existente.
- @Service removido de interfaces
- findById implementado corretamente
- Verbos HTTP corretos (@PostMapping, @GetMapping, etc.)
- ResponseEntity retornando recurso salvo com status 201
- java.util.Date substituído por LocalDateTime
- @Override desnecessários removidos do repository
- Código morto deletado
- URLs corrigidas para plural

### Fase 1 - Semana 1 - Concluída
CRUD completo de Task com verbos e status HTTP corretos.
Entidades Task, Category e Usuario mapeadas com JPA.
LocalDateTime em todos os campos de data.
Endpoints validados no Postman.

### Fase 1 - Semana 2 - Concluída
Validação e tratamento de erros centralizado.
- GlobalExceptionHandler com @ControllerAdvice cobrindo validação (MethodArgumentNotValidException), JSON malformado (HttpMessageConversionException), rota inexistente (NoResourceFoundException), verbo HTTP não suportado (HttpRequestMethodNotSupportedException) e recurso de negócio não encontrado (TaskNaoEncontradaException)
- ErroDTO e ErroValidacaoDTO padronizando timestamp, status, message, path; ErroValidacaoDTO lista os campos que falharam na validação
- TaskNaoEncontradaException criada e lançada via orElseThrow em selecionarTarefa, atualizarTarefa e deletarTarefa, unificando o contrato do TaskService (Optional<Task> substituído por Task direto nos três métodos)
- @Valid adicionado ao PUT (faltava desde o início do projeto)

### Fase 1 - Semana 3 - Concluída
Relacionamentos JPA, problema N+1, query customizada com JPQL.
- Task como dono das relações (@ManyToOne + @JoinColumn) com Usuario e Category; Usuario e Category como lado inverso (@OneToMany mappedBy)
- @JsonIgnore em Usuario.tasks e Category.tasks resolvendo loop infinito de serialização do relacionamento bidirecional
- UsuarioNaoEncontrado criada e lançada via orElseThrow em criarTarefa, com handler correspondente no GlobalExceptionHandler
- TaskRepository.consultarTodasDoUsuario com JOIN FETCH em JPQL, evitando N+1 na listagem de tarefas por usuário
- GET /tasks sem escopo removido (decisão de produto: listagem só faz sentido por usuário); consultarTodas() removido de TaskService, TaskServiceImpl e TaskController

**Pendências registradas, não bloqueiam, retomar quando conveniente**:
- CriarTaskDTO perdeu as mensagens customizadas do @NotBlank (voltou pro texto padrão do Bean Validation)
- DELETE retorna 200 com o corpo da tarefa deletada, fora do padrão mais comum (204 sem corpo), decisão de design a confirmar se foi intencional
- Import não usado: ResponseEntity em TaskServiceImpl
- @JoinColumn(name = "id_categoria" vs "categoria") e nomes de coluna em geral (id_user, id_categoria) sem convenção fixa entre si, vale padronizar
- Entidades ainda são expostas diretamente na resposta da API (Task, e por consequência Usuario/Category resumidos via @JsonIgnore); Semana 4 resolve isso com DTOs de resposta

---

## Em andamento

### Fase 1 - Semana 4

#### Backend
**Objetivo**: DTOs de request e response. Mapeamento com MapStruct. Flyway para versionamento do banco.
**Critério de aceite**:
- Nenhum endpoint expõe entidade JPA; request e response usam DTOs dedicados
- Conversão entidade <-> DTO feita por mapper MapStruct gerado, não manual
- Schema do banco em migrations Flyway versionadas; app sobe aplicando as migrations, sem criação de schema por ddl-auto
**Pendências anteriores**: as do Histórico, não bloqueiam.

#### Frontend
**Objetivo**: fundação do app React e camada de consumo da API. Ainda sem tela de regra de negócio.
**Correspondência com o backend**: consome os DTOs de resposta desta semana e o contrato de erro (ErroDTO / ErroValidacaoDTO) do backend Semana 2.
**Ordem dentro da semana**: começar por setup e camada de serviço contra o contrato atual; ajustar para os DTOs novos conforme o backend os entrega.
**Critério de aceite**:
- Projeto React (Vite) criado, rodando local, com estrutura definida (components, pages, services)
- Cliente HTTP único, base URL da API vinda de variável de ambiente
- services/ com função para cada endpoint atual: criar usuário; listar tarefas do usuário (GET /tasks/user/{id}); buscar tarefa (GET /tasks/{id}); criar (POST /tasks/user/{id}); atualizar (PUT /tasks/{id}); deletar (DELETE /tasks/{id})
- Tratamento central que lê o corpo de erro padronizado (status, message, campos de validação) e entrega para a UI
- README do frontend com passo de subida e a dependência do backend rodando

---

## Pendente

### Fase 1 - Semana 5

#### Backend
Paginação com Pageable. Filtros por parâmetros de consulta.
**Critério de aceite**:
- Listagem de tarefas do usuário paginada (page, size, sort) com metadados de paginação no corpo
- Filtros por query param (ex.: status, categoria) combináveis na mesma chamada

#### Frontend
Tela de listagem de tarefas com paginação e filtros.
**Correspondência com o backend**: consome o Pageable e os filtros por query param desta semana; consome o GET por usuário (backend Semana 1).
**Critério de aceite**:
- Tela lista tarefas de um usuário com dados reais da API
- Controles de página ligados aos parâmetros reais (page/size)
- Filtros por categoria e por status montam a query e refazem a chamada
- Estados de carregando, vazio e erro visíveis
- Seleção do usuário ativo (campo de id ou lista), já que a listagem é sempre por usuário

### Fase 1 - Semana 6

#### Backend
Revisão crítica da API. README no GitHub.

#### Frontend
Formulário de criar e editar tarefa, exclusão, seleção de categoria. Fechamento do MVP.
**Correspondência com o backend**: consome os request DTOs (Semana 4) e o CRUD completo (Semanas 1-3); renderiza erros de validação do ErroValidacaoDTO campo a campo.
**Critério de aceite**:
- Formulário cria e edita tarefa chamando os endpoints reais com o request DTO
- Erro de validação da API aparece no campo correspondente do formulário
- Excluir tarefa com confirmação; a lista atualiza após a operação
- Seleção de categoria no formulário (fonte a combinar com o backend)
- Troca de status da tarefa pela UI (status é string livre hoje; front e back precisam combinar os valores válidos)
- Revisão crítica do frontend e README no GitHub

**MVP fecha aqui**: o usuário abre o app, escolhe ou cria um usuário, cria uma tarefa com categoria, vê a tarefa na lista paginada, filtra, edita, troca o status para concluída e exclui, tudo contra a API, sem tocar no banco na mão.

**Critério de conclusão da Fase 1**: API funciona end-to-end, erros com resposta padronizada, relacionamentos JPA corretos, banco gerenciado pelo Flyway; e o frontend React consome a API com o fluxo de MVP acima funcionando; o desenvolvedor consegue explicar cada decisão de design do backend e como o front consome cada endpoint e trata erro, sem consultar o código.

### Fase 2 - Semanas 7-9

#### Backend
Testes automatizados. JUnit 5 + Mockito. Cobertura mínima de 60% na camada de serviço.

#### Frontend (pós-MVP)
Testes de componente e de fluxo de tela (ex.: React Testing Library) cobrindo criar, listar, editar e excluir, com a API mockada. Alvo de cobertura do front a definir.

### Fase 2 - Semanas 10-12

#### Backend
Autenticação e autorização com Spring Security + JWT.

#### Frontend (pós-MVP)
Tela de login consumindo o endpoint de autenticação, guarda do token, envio do token nas chamadas, rota protegida e logout. Consome direto o fluxo JWT do backend.

### Fase 2 - Semanas 13-14

#### Backend
Controle transacional com @Transactional. BigDecimal para valores monetários. Idempotência. SLF4J. Swagger/OpenAPI.

#### Frontend (pós-MVP)
Sem tela nova obrigatória. Enviar chave de idempotência quando o backend exigir; conferir os contratos consumidos contra o Swagger.

**Critério de conclusão da Fase 2**: testes com 60%+ de cobertura no backend, JWT funcionando, Swagger documentado, zero System.out.println, rollback de transação demonstrado na prática; frontend com login e chamadas autenticadas funcionando.

### Fase 3 - Semanas 15-18
Projeto solo com domínio escolhido pelo desenvolvedor. Backend e frontend seguem juntos no novo domínio.

### Fase 3 - Semanas 19-20

#### Backend
Docker + docker-compose.

#### Frontend (pós-MVP)
Dockerfile do app React (build estático servido por nginx ou equivalente) e serviço no mesmo docker-compose, subindo front, back e banco juntos.

### Fase 3 - Semanas 21-22
Preparação para entrevista técnica. Candidaturas.

---

## Log de sessões

A partir da Semana 4, marcar cada entrada com [BE] ou [FE].

| Data | O que foi feito | Pendências | Próximo passo |
|------|----------------|------------|---------------|
| Semana 1 | CRUD completo, entidades mapeadas, validado no Postman | - | Semana 2: validação e tratamento de erros |
| Semana 2 | GlobalExceptionHandler centralizado, ErroDTO/ErroValidacaoDTO, TaskNaoEncontradaException, @Valid no PUT | Mensagens customizadas do DTO perdidas; DELETE com status 200 em vez de 204; imports não usados | Semana 3: relacionamentos JPA, N+1, JPQL |
| Semana 3 | Relacionamentos JPA (Task dono, Usuario/Category inverso), @JsonIgnore no loop de serialização, UsuarioNaoEncontrado + handler, JOIN FETCH em JPQL pra evitar N+1, GET /tasks sem escopo removido | Pendências acumuladas da semana 2 ainda abertas; entidades ainda expostas direto na API | Semana 4: DTOs de request/response, MapStruct, Flyway |