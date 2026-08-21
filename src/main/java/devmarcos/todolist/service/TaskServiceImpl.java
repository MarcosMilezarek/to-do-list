package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.controller.CriarTaskDTO;
import devmarcos.todolist.exception.TaskNaoEncontradaException;
import devmarcos.todolist.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
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
    public List<Task> consultarTodas() {
        return taskRepository.findAll();
    }


}
