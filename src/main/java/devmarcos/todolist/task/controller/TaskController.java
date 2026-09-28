package devmarcos.todolist.task.controller;

import devmarcos.todolist.task.dto.TaskResponseDTO;
import devmarcos.todolist.task.dto.CriarTaskDTO;
import devmarcos.todolist.task.service.TaskService;
import devmarcos.todolist.task.model.Task;
import devmarcos.todolist.user.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PostMapping
    @ResponseBody
    public ResponseEntity<TaskResponseDTO> CadastrarTarefa(@RequestBody @Valid CriarTaskDTO criarTaskDTO, @AuthenticationPrincipal Usuario usuario) {
        Task tarefaSalva = taskService.criarTarefa(criarTaskDTO, usuario.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tarefaSalva.getId()).toUri();
        return ResponseEntity.created(location).body(TaskResponseDTO.from(tarefaSalva));

    }

    @GetMapping("/{idtarefa}")
    public ResponseEntity<TaskResponseDTO> listarTarefa(@PathVariable("idtarefa") Long idtarefa,  @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(TaskResponseDTO.from(taskService.selecionarTarefa(idtarefa, usuario.getId())));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> ExcluirTarefa(@PathVariable("id") Long id, @AuthenticationPrincipal Usuario usuario) {
        taskService.deletarTarefa(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> AtualizarTarefa(@PathVariable("id") Long id, @RequestBody @Valid CriarTaskDTO criarTaskDTO, @AuthenticationPrincipal Usuario usuario) {
        Task tarefaAtualizada = taskService.atualizarTarefa(criarTaskDTO, id, usuario.getId());
        return ResponseEntity.ok(TaskResponseDTO.from(tarefaAtualizada));
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getByUser(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoriaId,
            @AuthenticationPrincipal Usuario usuario,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(taskService.consultarTarefasPaginada(usuario.getId(), pageable, status, categoriaId));
    }
}

