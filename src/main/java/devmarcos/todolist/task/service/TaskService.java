package devmarcos.todolist.task.service;

import devmarcos.todolist.task.dto.TaskResponseDTO;
import devmarcos.todolist.task.dto.CriarTaskDTO;
import devmarcos.todolist.task.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Task criarTarefa(CriarTaskDTO criarTaskDTO, Long usuarioId);

    Task atualizarTarefa(CriarTaskDTO criarTaskDTO, Long id, Long usuarioId);

    Task deletarTarefa(Long id, Long usuarioId);

    Task selecionarTarefa(Long id, Long usuarioId);

    Page<TaskResponseDTO> consultarTarefasPaginada(Long usuarioId, Pageable pageable, String status, Long categoriaId);


}
