package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.controller.CriarTaskDTO;

import java.util.List;

public interface TaskService {

    String CriarTarefa(CriarTaskDTO criarTaskDTO);

    String AtualizarTarefa(CriarTaskDTO criarTaskDTO, Long id);

    String DeletarTarefa(Long id);

    Task SelecionarTarefa(Long id);

    List<Task> ConsultarTodas();

}
