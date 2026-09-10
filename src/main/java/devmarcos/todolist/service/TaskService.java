package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.dto.CriarTaskDTO;

import java.util.List;

public interface TaskService {

    Task criarTarefa(CriarTaskDTO criarTaskDTO);

    Task atualizarTarefa(CriarTaskDTO criarTaskDTO, Long id);

    Task deletarTarefa(Long id);

    Task selecionarTarefa(Long id);

    List<Task> consultarTodasDoUsuario(Long id_user);
}
