package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.controller.CriarTaskDTO;
import devmarcos.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task criarTarefa(CriarTaskDTO criarTaskDTO) {
        Task entidade = new Task(
                criarTaskDTO.descricao(),
                criarTaskDTO.status(),
                Instant.now(),
                null);
        return taskRepository.save(entidade);

    }

    @Override
    public Optional<Task> atualizarTarefa(CriarTaskDTO criarTaskDTO, Long id) {
        return taskRepository.findById(id).map(entidade -> {
            entidade.setDescricao(criarTaskDTO.descricao());
            entidade.setStatus(criarTaskDTO.status());
            return taskRepository.save(entidade);
        });
    }

    @Override
    public boolean deletarTarefa(Long id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }


    @Override
    public Optional<Task> selecionarTarefa(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> consultarTodas() {
        return taskRepository.findAll();
    }


}
