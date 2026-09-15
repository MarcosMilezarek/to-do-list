package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.dto.CriarTaskDTO;
import devmarcos.todolist.dto.TaskResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Task criarTarefa(CriarTaskDTO criarTaskDTO);

    Task atualizarTarefa(CriarTaskDTO criarTaskDTO, Long id);

    Task deletarTarefa(Long id);

    Task selecionarTarefa(Long id);

    Page<TaskResponseDTO> consultarTarefasPaginada(Long usuarioId, Pageable pageable, String status, Long categoriaId);


}
