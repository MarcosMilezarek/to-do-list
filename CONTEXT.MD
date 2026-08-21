# CONTEXT.md — Java Backend Junior

Arquivo de contexto do projeto. Lido pelo orientador no início de cada sessão.
Atualizado manualmente pelo desenvolvedor ao concluir etapas.

---

## Estado atual

**Fase**: Fase 1 - API completa com Spring Boot + JPA
**Semana em andamento**: Semana 3
**Próximo desafio**: Relacionamentos JPA entre entidades, problema N+1, query customizada com JPQL

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

**Pendências registradas, não bloqueiam, retomar quando conveniente**:
- CriarTaskDTO perdeu as mensagens customizadas do @NotBlank (voltou pro texto padrão do Bean Validation)
- DELETE retorna 200 com o corpo da tarefa deletada, fora do padrão mais comum (204 sem corpo), decisão de design a confirmar se foi intencional
- Imports não usados: Optional em TaskService e TaskServiceImpl, ResponseEntity em TaskServiceImpl, Valid em CriarTaskDTO

---

## Em andamento

### Fase 1 - Semana 3
**Objetivo**: Relacionamentos JPA entre entidades, problema N+1, query customizada com JPQL

**Pendências da semana anterior**: as três listadas acima, não bloqueiam o início da semana 3

---

## Pendente

### Fase 1 - Semana 4
DTOs de request e response. Mapeamento com MapStruct. Flyway para versionamento do banco.

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