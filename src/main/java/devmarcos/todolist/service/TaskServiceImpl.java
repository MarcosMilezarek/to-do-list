package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.controller.CriarTaskDTO;
import devmarcos.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

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
    public String AtualizarTarefa(CriarTaskDTO criarTaskDTO, Long id) {

        var TaskAtualizado = taskRepository.findById(id);

        if (TaskAtualizado.isPresent()) {
            var entidade = TaskAtualizado.get();
            entidade.setDescricao(criarTaskDTO.descricao().toString());
            entidade.setStatus(criarTaskDTO.status().toString());
            taskRepository.save(entidade);
            return "Tarefa Atualizada";

        } else {
            return "Nenhuma tarefa encontrada!";
        }


    }

    @Override
    public String DeletarTarefa(Long id) {
        taskRepository.deleteById(id);
        return "Tarefa removida com sucesso";
    }

    @Override
    public Task SelecionarTarefa(Long id) {
        return taskRepository.findById(id).get();
    }

    @Override
    public List<Task> ConsultarTodas() {
        return taskRepository.findAll();
    }
}
