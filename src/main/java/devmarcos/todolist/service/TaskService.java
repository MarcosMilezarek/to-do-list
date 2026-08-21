package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.controller.CriarTaskDTO;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task criarTarefa(CriarTaskDTO criarTaskDTO);

    Task atualizarTarefa(CriarTaskDTO criarTaskDTO, Long id);

    Task deletarTarefa(Long id);

    Task selecionarTarefa(Long id);

    List<Task> consultarTodas();

}
