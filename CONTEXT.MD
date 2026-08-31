# CONTEXT.md — Java Backend Junior

Arquivo de contexto do projeto. Lido pelo orientador no início de cada sessão.
Atualizado manualmente pelo desenvolvedor ao concluir etapas.

---

## Estado atual

**Fase**: Fase 1 - API completa com Spring Boot + JPA
**Semana em andamento**: Semana 4
**Próximo desafio**: DTOs de request e response, mapeamento com MapStruct, Flyway para versionamento do banco

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
**Objetivo**: DTOs de request e response. Mapeamento com MapStruct. Flyway para versionamento do banco.

**Pendências da semana anterior**: as listadas acima, não bloqueiam o início da semana 4

---

## Pendente

### Fase 1 - Semana 5
Paginação com Pageable. Filtros por parâmetros de consulta.

### Fase 1 - Semana 6
Revisão crítica da API. README no GitHub.

**Critério de conclusão da Fase 1**: API funciona end-to-end, erros com resposta padronizada, relacionamentos JPA corretos, banco gerenciado pelo Flyway, desenvolvedor consegue explicar cada decisão de design sem consultar o código.

### Fase 2 - Semanas 7-9
Testes automatizados. JUnit 5 + Mockito. Cobertura mínima de 60% na camada de serviço.

### Fase 2 - Semanas 10-12
Autenticação e autorização com Spring Security + JWT.

### Fase 2 - Semanas 13-14
Controle transacional com @Transactional. BigDecimal para valores monetários. Idempotência. SLF4J. Swagger/OpenAPI.

**Critério de conclusão da Fase 2**: testes com 60%+ de cobertura, JWT funcionando, Swagger documentado, zero System.out.println, rollback de transação demonstrado na prática.

### Fase 3 - Semanas 15-18
Projeto solo com domínio escolhido pelo desenvolvedor.

### Fase 3 - Semanas 19-20
Docker + docker-compose.

### Fase 3 - Semanas 21-22
Preparação para entrevista técnica. Candidaturas.

---

## Log de sessões

| Data | O que foi feito | Pendências | Próximo passo |
|------|----------------|------------|---------------|
| Semana 1 | CRUD completo, entidades mapeadas, validado no Postman | - | Semana 2: validação e tratamento de erros |
| Semana 2 | GlobalExceptionHandler centralizado, ErroDTO/ErroValidacaoDTO, TaskNaoEncontradaException, @Valid no PUT | Mensagens customizadas do DTO perdidas; DELETE com status 200 em vez de 204; imports não usados | Semana 3: relacionamentos JPA, N+1, JPQL |
| Semana 3 | Relacionamentos JPA (Task dono, Usuario/Category inverso), @JsonIgnore no loop de serialização, UsuarioNaoEncontrado + handler, JOIN FETCH em JPQL pra evitar N+1, GET /tasks sem escopo removido | Pendências acumuladas da semana 2 ainda abertas; entidades ainda expostas direto na API | Semana 4: DTOs de request/response, MapStruct, Flyway |