package devmarcos.todolist.service;
import devmarcos.todolist.Model.Task;
import devmarcos.todolist.dto.CriarTaskDTO;
import devmarcos.todolist.dto.TaskResponseDTO;
import devmarcos.todolist.exception.UsuarioNaoEncontrado;
import devmarcos.todolist.exception.TaskNaoEncontradaException;
import devmarcos.todolist.repository.TaskRepository;
import devmarcos.todolist.repository.UserRepository;
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
    public Task criarTarefa(CriarTaskDTO criarTaskDTO) {
        Task entidade = new Task(
                criarTaskDTO.descricao(),
                criarTaskDTO.status(),
                Instant.now(),
                null);

        entidade.setUsuario(userRepository.findById(criarTaskDTO.id_user())
                .orElseThrow(() -> new UsuarioNaoEncontrado(criarTaskDTO.id_user())));

        return taskRepository.save(entidade);

    }

    @Override
    public Task atualizarTarefa(CriarTaskDTO criarTaskDTO, Long id) {
        Task entidade = taskRepository.findById(id).orElseThrow(() -> new TaskNaoEncontradaException(id));
        entidade.setDescricao(criarTaskDTO.descricao());
        entidade.setStatus(criarTaskDTO.status());
        return taskRepository.save(entidade);
    }

    @Override
    public Task deletarTarefa(Long id) {
        Task tarefaExiste =  taskRepository.findById(id).orElseThrow(() -> new TaskNaoEncontradaException(id));
        taskRepository.delete(tarefaExiste);
        return tarefaExiste;
    }


    @Override
    public Task selecionarTarefa(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNaoEncontradaException(id));
    }

    @Override
    public Page<TaskResponseDTO> consultarTarefasPaginada(Long usuarioId, Pageable pageable, String status, Long categoriaId) {
        Page<Task> tasks = taskRepository.consultarTarefasPaginadaDoUsuario(usuarioId,status, categoriaId, pageable);
        return tasks.map(TaskResponseDTO::from);
    }


}
