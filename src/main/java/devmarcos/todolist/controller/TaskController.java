package devmarcos.todolist.controller;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


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
    public ResponseEntity<Task> CadastrarTarefa(@RequestBody @Valid CriarTaskDTO criarTaskDTO) {

        Task tarefaSalva = taskService.criarTarefa(criarTaskDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tarefaSalva.getId()).toUri();

        return ResponseEntity.created(location).body(tarefaSalva);

    }


    @GetMapping("/{idtarefa}")
    public ResponseEntity<Task> listarTarefa(@PathVariable("idtarefa") Long idtarefa) {
        return taskService.selecionarTarefa(idtarefa).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAll() {
        List<Task> consulta = taskService.consultarTodas();
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> ExcluirTarefa(@PathVariable("id") Long id) {
        boolean deletado = taskService.deletarTarefa(id);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Task> AtualizarTarefa(@PathVariable("id") Long id, @RequestBody CriarTaskDTO criarTaskDTO) {
        return taskService.atualizarTarefa(criarTaskDTO, id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

