package devmarcos.todolist.task.repository;

import devmarcos.todolist.task.model.Category;
import devmarcos.todolist.task.model.Task;
import devmarcos.todolist.user.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import java.time.Instant;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TestEntityManager entityManager;

    private Usuario marcos;
    private Usuario ana;
    private Category estudos;

    @BeforeEach
    void prepararDados() {
        marcos = entityManager.persist(new Usuario("Marcos", "marcos@exemplo.com", Instant.now(), null, "hash"));
        ana = entityManager.persist(new Usuario("Ana", "ana@exemplo.com", Instant.now(), null, "hash"));
        estudos = entityManager.persist(new Category("Estudos"));

        salvarTarefa("Estudar Spring", "PENDENTE", marcos, estudos);
        salvarTarefa("Revisar JPA", "CONCLUIDA", marcos, null);
        salvarTarefa("Ler sobre testes", "PENDENTE", marcos, null);
        salvarTarefa("Tarefa da Ana", "PENDENTE", ana, estudos);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void consultarTarefasPaginadaSomenteDoUsuario() {
        Page<Task> resultado = taskRepository.consultarTarefasPaginadaDoUsuario(marcos.getId(), null, null, PageRequest.of(0, 10));

        assertEquals(3, resultado.getTotalElements());
        assertTrue(resultado.getContent().stream().allMatch(t -> t.getUsuario().getId().equals(marcos.getId())));
    }

    @Test
    void consultarTarefasPaginadaOutroUsuario() {
        Page<Task> resultado = taskRepository.consultarTarefasPaginadaDoUsuario(ana.getId(), null, null, PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
        assertEquals("Tarefa da Ana", resultado.getContent().get(0).getDescricao());
    }

    @Test
    void consultarTarefasPaginadaFiltroPorStatus() {
        Page<Task> resultado = taskRepository.consultarTarefasPaginadaDoUsuario(marcos.getId(), "PENDENTE", null, PageRequest.of(0, 10));

        assertEquals(2, resultado.getTotalElements());
        assertTrue(resultado.getContent().stream().allMatch(t -> t.getStatus().equals("PENDENTE")));
    }

    @Test
    void consultarTarefasPaginadaFiltroPorCategoria() {
        Page<Task> resultado = taskRepository.consultarTarefasPaginadaDoUsuario(marcos.getId(), null, estudos.getId(), PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
        assertEquals("Estudar Spring", resultado.getContent().get(0).getDescricao());
        assertEquals("Estudos", resultado.getContent().get(0).getCategoria().getNome());
    }

    @Test
    void consultarTarefasPaginadaRespeitaTamanhoDaPagina() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id"));

        Page<Task> resultado = taskRepository.consultarTarefasPaginadaDoUsuario(marcos.getId(), null, null, pageable);

        assertEquals(2, resultado.getContent().size());
        assertEquals(3, resultado.getTotalElements());
        assertEquals(2, resultado.getTotalPages());
        assertEquals("Estudar Spring", resultado.getContent().get(0).getDescricao());
    }

    @Test
    void findByIdAndUsuarioIdTarefaDeOutroUsuario() {
        Task tarefaDaAna = taskRepository.consultarTarefasPaginadaDoUsuario(ana.getId(), null, null, PageRequest.of(0, 1)).getContent().get(0);

        assertTrue(taskRepository.findByIdAndUsuarioId(tarefaDaAna.getId(), marcos.getId()).isEmpty());
        assertTrue(taskRepository.findByIdAndUsuarioId(tarefaDaAna.getId(), ana.getId()).isPresent());
    }

    private void salvarTarefa(String descricao, String status, Usuario usuario, Category categoria) {
        Task tarefa = new Task(descricao, status, Instant.now(), null);
        tarefa.setUsuario(usuario);
        tarefa.setCategoria(categoria);
        entityManager.persist(tarefa);
    }
}
