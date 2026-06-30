package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.controller.CriarTaskDTO;
import devmarcos.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public String CriarTarefa(CriarTaskDTO criarTaskDTO) {
        Task entidade = new Task(criarTaskDTO.descricao(), criarTaskDTO.status(), Instant.now(), null);
        taskRepository.save(entidade);


        return "Task criada com sucesso!\n" + "[" + entidade.getId().toString() + "] " + entidade.getDescricao();


    }

    @Override
    public String AtualizarTarefa(Task task) {
        return "";
    }

    @Override
    public String DeletarTarefa(Task task) {
        taskRepository.delete(task);
        return "Tarefa removida com sucesso";
    }

    @Override
    public List<Task> SelecionarTarefa(Integer id) {
        return taskRepository.findById(id).stream().toList();
    }
}
