package devmarcos.todolist.task.service.impl;

import devmarcos.todolist.task.dto.TaskResponseDTO;
import devmarcos.todolist.task.dto.CriarTaskDTO;
import devmarcos.todolist.task.exception.TaskNaoEncontradaException;
import devmarcos.todolist.task.repository.TaskRepository;
import devmarcos.todolist.task.service.TaskService;
import devmarcos.todolist.task.model.Task;
import devmarcos.todolist.user.exception.UsuarioNaoEncontrado;
import devmarcos.todolist.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.Instant;



@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task criarTarefa(CriarTaskDTO criarTaskDTO, Long usuarioId) {
        Task entidade = new Task(
                criarTaskDTO.descricao(),
                criarTaskDTO.status(),
                Instant.now(),
                null);

        entidade.setUsuario(userRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado(usuarioId)));

        return taskRepository.save(entidade);

    }

    @Override
    public Task atualizarTarefa(CriarTaskDTO criarTaskDTO, Long id, Long usuarioId) {
        Task entidade = taskRepository.findByIdAndUsuarioId(id, usuarioId).orElseThrow(() -> new TaskNaoEncontradaException(id));
        entidade.setDescricao(criarTaskDTO.descricao());
        entidade.setStatus(criarTaskDTO.status());
        return taskRepository.save(entidade);
    }

    @Override
    public Task deletarTarefa(Long id, Long usuarioId) {
        Task tarefaExiste =  taskRepository.findByIdAndUsuarioId(id, usuarioId).orElseThrow(() -> new TaskNaoEncontradaException(id));
        taskRepository.delete(tarefaExiste);
        return tarefaExiste;
    }


    @Override
    public Task selecionarTarefa(Long id, Long usuarioId) {
        return taskRepository.findByIdAndUsuarioId(id, usuarioId).orElseThrow(() -> new TaskNaoEncontradaException(id));
    }

    @Override
    public Page<TaskResponseDTO> consultarTarefasPaginada(Long usuarioId, Pageable pageable, String status, Long categoriaId) {
        Page<Task> tasks = taskRepository.consultarTarefasPaginadaDoUsuario(usuarioId,status, categoriaId, pageable);
        return tasks.map(TaskResponseDTO::from);
    }


}
