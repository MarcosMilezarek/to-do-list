package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.controller.CriarTaskDTO;

import java.util.List;

public interface TaskService {

    String CriarTarefa(CriarTaskDTO criarTaskDTO);

    String AtualizarTarefa(Task task);

    String DeletarTarefa(Task task);

    List<Task> SelecionarTarefa(Integer id);

}
