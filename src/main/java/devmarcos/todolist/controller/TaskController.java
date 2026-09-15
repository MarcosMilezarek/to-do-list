package devmarcos.todolist.controller;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.dto.CriarTaskDTO;
import devmarcos.todolist.dto.TaskResponseDTO;
import devmarcos.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;



@RestController
@RequestMapping("/tasks")
public class TaskController {


    TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    //    metodo de adicioanr algo no bd sempre vai ser POST pra responder a requisição
    @PostMapping("/user/{id_user}")
    @ResponseBody
    public ResponseEntity<TaskResponseDTO> CadastrarTarefa(@RequestBody @Valid CriarTaskDTO criarTaskDTO, @PathVariable("id_user") Long user_id) {
        criarTaskDTO  = new CriarTaskDTO(criarTaskDTO.descricao(), criarTaskDTO.status(), user_id);
        Task tarefaSalva = taskService.criarTarefa(criarTaskDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tarefaSalva.getId()).toUri();
        return ResponseEntity.created(location).body(TaskResponseDTO.from(tarefaSalva));

    }

    @GetMapping("/{idtarefa}")
    public ResponseEntity<TaskResponseDTO> listarTarefa(@PathVariable("idtarefa") Long idtarefa) {
        return ResponseEntity.ok(TaskResponseDTO.from(taskService.selecionarTarefa(idtarefa)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> ExcluirTarefa(@PathVariable("id") Long id) {
        taskService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> AtualizarTarefa(@PathVariable("id") Long id, @RequestBody @Valid CriarTaskDTO criarTaskDTO) {
        Task tarefaAtualizada = taskService.atualizarTarefa(criarTaskDTO, id);
        return ResponseEntity.ok(TaskResponseDTO.from(tarefaAtualizada));
    }

    @GetMapping("/user/{id_user}")
    public ResponseEntity<Page<TaskResponseDTO>> getByUser(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoriaId,
            @PathVariable("id_user") Long id_user,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(taskService.consultarTarefasPaginada(id_user, pageable, status, categoriaId));
    }
}

