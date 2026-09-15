# CONTEXT.md — Java Backend Junior

Arquivo de contexto do projeto. Lido pelo orientador no início de cada sessão.
Mantido pelo agente de backend a cada sessão; o desenvolvedor aprova cada alteração antes de aplicar.

---

## Estado atual

**Fase**: Fase 1 - API completa com Spring Boot + JPA. Trilha de frontend em React entra em paralelo a partir da Semana 4.
**Semana em andamento**: Semana 5

**Trilhas e agentes**:
- **Backend** (agente próprio): construção da API e manutenção deste arquivo. Semanas 1-4 entregaram CRUD de Task, tratamento de erro centralizado, relacionamentos JPA e DTOs de request/response com Flyway.
- **Frontend**: app React que só consome os serviços do backend. Sem regra de negócio no front. [Responsável a confirmar: agente próprio ainda ativo ou trilha absorvida pelo desenvolvedor.]
- **Plano de estudos / contexto macro**: descontinuado. Manutenção do cronograma e deste arquivo passou para o agente de backend.

**Objetivo da trilha frontend**: MVP do to-do list, fechado ao fim da Fase 1 (Semana 6), funcionando end-to-end contra a API.

**Regra de sincronização**: toda entrega do backend tem entrega correspondente no frontend na mesma semana ou na seguinte.

**Próximo desafio backend**: paginação com Pageable e filtros por query param na listagem de tarefas do usuário.
**Próximo desafio frontend**: setup do projeto React e camada de serviço consumindo a API.

**Local do código frontend**: a definir.

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
- TaskNaoEncontradaException lançada via orElseThrow em selecionarTarefa, atualizarTarefa e deletarTarefa, unificando o contrato do TaskService
- @Valid adicionado ao PUT

### Fase 1 - Semana 3 - Concluída
Relacionamentos JPA, problema N+1, query customizada com JPQL.
- Task como dono das relações (@ManyToOne + @JoinColumn) com Usuario e Category; Usuario e Category como lado inverso (@OneToMany mappedBy)
- @JsonIgnore em Usuario.tasks e Category.tasks resolvendo loop infinito de serialização do relacionamento bidirecional
- UsuarioNaoEncontrado lançada via orElseThrow em criarTarefa, com handler no GlobalExceptionHandler
- TaskRepository.consultarTodasDoUsuario com JOIN FETCH em JPQL, evitando N+1 na listagem por usuário
- GET /tasks sem escopo removido (decisão de produto: listagem só faz sentido por usuário)

### Fase 1 - Semana 4 - Concluída com desvio
DTOs de request e response; Flyway.
- Todos os endpoints de TaskController e UserController usam DTO dedicado em request e response; nenhuma entidade JPA é exposta na API
- CriarTaskDTO passou a carregar id_user; POST /tasks/user/{id} associa a tarefa ao usuário via orElseThrow com UsuarioNaoEncontrado
- Flyway integrado: dependência flyway-core + flyway-mysql, V1__criar_schema_inicial.sql criando usuario, category e Tarefas; ddl-auto mudado de update para validate; app sobe aplicando a migration
- MapStruct removido do pom.xml por decisão do desenvolvedor

**Desvio registrado**: o critério "conversão entidade <-> DTO feita por mapper MapStruct gerado, não manual" não foi cumprido. A conversão é manual (TaskResponseDTO.from(), UsuarioResumoDTO montado inline no controller). MapStruct vira dívida técnica, a reavaliar quando a repetição do mapeamento manual justificar, provavelmente na Semana 6.

---

## Dívida técnica acumulada

Não bloqueia o avanço. Material natural da revisão crítica da Semana 6.
- Conversão entidade/DTO manual; MapStruct adiado (Semana 4)
- CriarTaskDTO perdeu as mensagens customizadas do @NotBlank (voltou ao texto padrão do Bean Validation)
- DELETE /tasks/{id} retorna 200 com corpo em vez de 204 sem corpo; confirmar se é intencional
- Nomes de coluna sem convenção fixa entre si (id_user, id_categoria); padronizar
- UserServiceImpl.findById é stub retornando null
- CreateUserDTO aceita creationTime/modificationTime do cliente; o servidor deveria gerar
- getByUser em TaskController tem variável local `lista` morta e import ArrayList não usado
- UserController monta UsuarioResumoDTO inline em dois lugares; falta um UsuarioResumoDTO.from(Usuario)
- @JsonIgnore em Usuario.tasks / Category.tasks é workaround do loop de serialização; com DTOs de resposta existindo, reavaliar se as entidades ainda precisam ser serializáveis
- TaskController reatribui o parâmetro criarTaskDTO em vez de passar o id como argumento separado ao service

---

## Em andamento

### Fase 1 - Semana 5

#### Backend
**Objetivo**: paginação com Pageable e filtros por parâmetro de consulta na listagem de tarefas do usuário.
**Critério de aceite**:
- GET /tasks/user/{id} devolve resposta paginada com metadados (número da página, tamanho, total de elementos, total de páginas) no corpo, controlada por page, size e sort na query
- Filtros opcionais por query param (ao menos status e categoria) que se combinam na mesma chamada; ausência de filtro devolve tudo do usuário, paginado
- A conversão para TaskResponseDTO é preservada na resposta paginada (nenhuma entidade exposta)
  **O que não usar**: carregar a lista inteira do banco e paginar em memória; a paginação tem que descer pro repositório (Page<Task> / consulta paginada).
  **Estimativa**: uma sessão de sábado ou duas a três sessões curtas.

#### Frontend
**Objetivo**: fundação do app React e camada de consumo da API. Ainda sem tela de regra de negócio.
**Correspondência com o backend**: consome os DTOs de resposta da Semana 4 e o contrato de erro (ErroDTO / ErroValidacaoDTO) da Semana 2.
**Critério de aceite**:
- Projeto React (Vite) criado, rodando local, com estrutura definida (components, pages, services)
- Cliente HTTP único, base URL da API vinda de variável de ambiente
- services/ com função para cada endpoint atual: criar usuário; login por email; listar tarefas do usuário; buscar tarefa; criar tarefa; atualizar; deletar
- Tratamento central que lê o corpo de erro padronizado e entrega para a UI
- README do frontend com passo de subida e a dependência do backend rodando

---

## Pendente

### Fase 1 - Semana 6

#### Backend
Revisão crítica da API e README no GitHub. Endereçar a dívida técnica acumulada. Reavaliar MapStruct.
**Critério de aceite**:
- Contratos, nomes, status HTTP e DTOs revisados e consistentes em toda a API
- Itens da dívida técnica ou resolvidos ou explicitamente aceitos com justificativa
- README no GitHub com propósito, stack, como subir (banco, migrations, app) e exemplos de request

#### Frontend
Formulário de criar e editar tarefa, exclusão, seleção de categoria. Fechamento do MVP.
**Critério de aceite**:
- Formulário cria e edita tarefa chamando os endpoints reais com o request DTO
- Erro de validação da API aparece no campo correspondente do formulário
- Excluir tarefa com confirmação; a lista atualiza após a operação
- Seleção de categoria no formulário
- Troca de status da tarefa pela UI (combinar valores válidos entre front e back)
- Revisão crítica do frontend e README no GitHub

**MVP fecha aqui**: o usuário abre o app, escolhe ou cria um usuário, cria uma tarefa com categoria, vê a tarefa na lista paginada, filtra, edita, troca o status para concluída e exclui, tudo contra a API.

**Critério de conclusão da Fase 1**: API funciona end-to-end, erros com resposta padronizada, relacionamentos JPA corretos, banco gerenciado pelo Flyway; frontend React consome a API com o fluxo de MVP acima; o desenvolvedor explica cada decisão de design do backend e como o front consome cada endpoint e trata erro, sem consultar o código.

### Fase 2 - Semanas 7-9

#### Backend
Testes automatizados. JUnit 5 + Mockito. Cobertura mínima de 60% na camada de serviço, incluindo os caminhos de exceção.

#### Frontend (pós-MVP)
Testes de componente e de fluxo de tela cobrindo criar, listar, editar e excluir, com a API mockada.

### Fase 2 - Semanas 10-12

#### Backend
Autenticação e autorização com Spring Security + JWT. O id do usuário deixa de vir da URL ou do corpo e passa a vir do token; endpoints que hoje recebem id_user explícito são redesenhados.

#### Frontend (pós-MVP)
Tela de login, guarda do token, envio do token nas chamadas, rota protegida e logout.

### Fase 2 - Semanas 13-14

#### Backend
Controle transacional com @Transactional. BigDecimal para valores monetários se o domínio pedir. Idempotência. SLF4J no lugar de qualquer System.out restante. Swagger/OpenAPI.

#### Frontend (pós-MVP)
Sem tela nova obrigatória. Enviar chave de idempotência quando o backend exigir; conferir contratos contra o Swagger.

**Critério de conclusão da Fase 2**: testes com 60%+ de cobertura no backend, JWT funcionando, Swagger documentado, zero System.out.println, rollback de transação demonstrado na prática; frontend com login e chamadas autenticadas funcionando.

### Fase 3 - Semanas 15-18
Projeto solo com domínio escolhido pelo desenvolvedor. Backend e frontend seguem juntos no novo domínio.

### Fase 3 - Semanas 19-20

#### Backend
Docker + docker-compose.

#### Frontend (pós-MVP)
Dockerfile do app React e serviço no mesmo docker-compose, subindo front, back e banco juntos.

### Fase 3 - Semanas 21-22
Preparação para entrevista técnica. Candidaturas.

---

## Log de sessões

A partir da Semana 4, marcar cada entrada com [BE] ou [FE].

| Data | O que foi feito | Pendências | Próximo passo |
|------|----------------|------------|---------------|
| Semana 1 | CRUD completo, entidades mapeadas, validado no Postman | - | Semana 2: validação e tratamento de erros |
| Semana 2 | GlobalExceptionHandler centralizado, ErroDTO/ErroValidacaoDTO, TaskNaoEncontradaException, @Valid no PUT | Mensagens customizadas do DTO perdidas; DELETE 200 em vez de 204 | Semana 3: relacionamentos JPA, N+1, JPQL |
| Semana 3 | Relacionamentos JPA (Task dono, Usuario/Category inverso), @JsonIgnore no loop de serialização, UsuarioNaoEncontrado + handler, JOIN FETCH em JPQL, GET /tasks sem escopo removido | Dívida da Semana 2 aberta; entidades ainda expostas na API | Semana 4: DTOs de request/response, MapStruct, Flyway |
| Semana 4 [BE] | DTOs de request/response em todos os endpoints, nenhuma entidade exposta; CriarTaskDTO com id_user; Flyway com V1 inicial, ddl-auto=validate; MapStruct removido | MapStruct adiado (desvio do critério); dívida técnica consolidada em seção própria | Semana 5: paginação com Pageable e filtros por query param |