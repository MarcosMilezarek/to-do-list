package devmarcos.todolist.task.service.impl;

import devmarcos.todolist.task.dto.TaskResponseDTO;
import devmarcos.todolist.task.repository.TaskRepository;
import devmarcos.todolist.task.model.Task;
import devmarcos.todolist.user.model.Usuario;
import devmarcos.todolist.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;


    @Test
    void consultarTarefasPaginadaSucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Marcos");

        Task tarefa = new Task("Estudar Spring", "PENDENTE", null, null);
        tarefa.setId(10L);
        tarefa.setUsuario(usuario);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> paginaFalsa = new PageImpl<>(List.of(tarefa), pageable, 1);
        TaskServiceImpl service = new TaskServiceImpl(taskRepository, userRepository);

        when(taskRepository.consultarTarefasPaginadaDoUsuario(1L, "PENDENTE", null, pageable))
                .thenReturn(paginaFalsa);

        Page<TaskResponseDTO> resultado = service.consultarTarefasPaginada(1L, pageable, "PENDENTE", null);

        assertEquals(1, resultado.getTotalElements());
        assertEquals("Estudar Spring", resultado.getContent().get(0).descricao());
    }
}
