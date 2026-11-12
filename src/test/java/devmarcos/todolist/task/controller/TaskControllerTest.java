package devmarcos.todolist.task.controller;

import devmarcos.todolist.login.config.SecurityConfig;
import devmarcos.todolist.login.config.TokenService;
import devmarcos.todolist.task.dto.CriarTaskDTO;
import devmarcos.todolist.task.dto.TaskResponseDTO;
import devmarcos.todolist.task.exception.TaskNaoEncontradaException;
import devmarcos.todolist.task.model.Task;
import devmarcos.todolist.task.service.TaskService;
import devmarcos.todolist.user.model.Usuario;
import devmarcos.todolist.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@Import(SecurityConfig.class)
class TaskControllerTest {

    private static final String TOKEN = "token-valido";
    private static final String TAREFA_JSON = """
            {"descricao": "Estudar testes", "status": "PENDENTE"}
            """;

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TaskService taskService;
    @MockitoBean
    private TokenService tokenService;
    @MockitoBean
    private UserRepository userRepository;

    private Usuario usuario;

    @BeforeEach
    void autenticarUsuario() {
        usuario = new Usuario("Marcos", "marcos@exemplo.com", null, null, "hash");
        usuario.setId(1L);
        when(tokenService.validateToken(TOKEN)).thenReturn(usuario.getEmail());
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(usuario);
    }

    @Test
    void cadastrarTarefaSucesso() throws Exception {
        when(taskService.criarTarefa(any(CriarTaskDTO.class), eq(1L))).thenReturn(tarefa(10L, "Estudar testes"));

        mockMvc.perform(post("/tasks").header("Authorization", "Bearer " + TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).content(TAREFA_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/tasks/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.descricao").value("Estudar testes"))
                .andExpect(jsonPath("$.usuario.nome").value("Marcos"));
    }

    @Test
    void cadastrarTarefaCamposEmBranco() throws Exception {
        mockMvc.perform(post("/tasks").header("Authorization", "Bearer " + TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"descricao\": \"\", \"status\": \" \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.descricao").value("Descrição é obrigatória"))
                .andExpect(jsonPath("$.erros.status").value("Status é obrigatório"));

        verifyNoInteractions(taskService);
    }

    @Test
    void cadastrarTarefaJsonMalFormado() throws Exception {
        mockMvc.perform(post("/tasks").header("Authorization", "Bearer " + TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"descricao\": "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Json informado está incorreto"));
    }

    @Test
    void cadastrarTarefaSemToken() throws Exception {
        mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(TAREFA_JSON))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(taskService);
    }

    @Test
    void cadastrarTarefaTokenInvalido() throws Exception {
        when(tokenService.validateToken("token-expirado")).thenReturn("");

        mockMvc.perform(post("/tasks").header("Authorization", "Bearer token-expirado")
                        .contentType(MediaType.APPLICATION_JSON).content(TAREFA_JSON))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(taskService);
    }

    @Test
    void selecionarTarefaSucesso() throws Exception {
        when(taskService.selecionarTarefa(10L, 1L)).thenReturn(tarefa(10L, "Estudar testes"));

        mockMvc.perform(get("/tasks/10").header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void selecionarTarefaInexistente() throws Exception {
        when(taskService.selecionarTarefa(99L, 1L)).thenThrow(new TaskNaoEncontradaException(99L));

        mockMvc.perform(get("/tasks/99").header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tarefa com id 99 não encontrada."));
    }

    @Test
    void selecionarTarefaSemToken() throws Exception {
        mockMvc.perform(get("/tasks/10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void atualizarTarefaSucesso() throws Exception {
        when(taskService.atualizarTarefa(any(CriarTaskDTO.class), eq(10L), eq(1L))).thenReturn(tarefa(10L, "Estudar testes"));

        mockMvc.perform(put("/tasks/10").header("Authorization", "Bearer " + TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).content(TAREFA_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Estudar testes"));
    }

    @Test
    void atualizarTarefaInexistente() throws Exception {
        when(taskService.atualizarTarefa(any(CriarTaskDTO.class), eq(99L), eq(1L))).thenThrow(new TaskNaoEncontradaException(99L));

        mockMvc.perform(put("/tasks/99").header("Authorization", "Bearer " + TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).content(TAREFA_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluirTarefaSucesso() throws Exception {
        mockMvc.perform(delete("/tasks/10").header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isNoContent());

        verify(taskService).deletarTarefa(10L, 1L);
    }

    @Test
    void excluirTarefaInexistente() throws Exception {
        when(taskService.deletarTarefa(99L, 1L)).thenThrow(new TaskNaoEncontradaException(99L));

        mockMvc.perform(delete("/tasks/99").header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isNotFound());
    }

    @Test
    void consultarTarefasPaginadaSucesso() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id"));
        when(taskService.consultarTarefasPaginada(eq(1L), any(Pageable.class), eq("PENDENTE"), eq(null)))
                .thenReturn(new PageImpl<>(List.of(TaskResponseDTO.from(tarefa(10L, "Estudar testes"))), pageable, 1));

        mockMvc.perform(get("/tasks").param("status", "PENDENTE").param("size", "5")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].descricao").value("Estudar testes"))
                .andExpect(jsonPath("$.content.length()").value(1));

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(taskService).consultarTarefasPaginada(eq(1L), captor.capture(), eq("PENDENTE"), eq(null));
        assertEquals(5, captor.getValue().getPageSize());
        assertEquals(Sort.Direction.ASC, captor.getValue().getSort().getOrderFor("id").getDirection());
    }

    private Task tarefa(Long id, String descricao) {
        Task tarefa = new Task(descricao, "PENDENTE", null, null);
        tarefa.setId(id);
        tarefa.setUsuario(usuario);
        return tarefa;
    }
}
