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
    @PostMapping("/user/{id_user}")
    @ResponseBody
    public ResponseEntity<Task> CadastrarTarefa(@RequestBody @Valid CriarTaskDTO criarTaskDTO, @PathVariable("id_user")Long user_id) {
        
        Task tarefaSalva = taskService.criarTarefa(criarTaskDTO,  user_id);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tarefaSalva.getId()).toUri();
        return ResponseEntity.created(location).body(tarefaSalva);

    }

    @GetMapping("/{idtarefa}")
    public ResponseEntity<Task> listarTarefa(@PathVariable("idtarefa") Long idtarefa) {
        return ResponseEntity.ok(taskService.selecionarTarefa(idtarefa));
    }

    @GetMapping("/user/{id_user}")
    public ResponseEntity<List<Task>> getByUser(@PathVariable("id_user") Long id_user) {
        List<Task> consulta = taskService.consultarTodasDoUsuario(id_user);
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> ExcluirTarefa(@PathVariable("id") Long id) {
        Task deletado = taskService.deletarTarefa(id);
        return ResponseEntity.ok(deletado);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Task> AtualizarTarefa(@PathVariable("id") Long id, @RequestBody @Valid CriarTaskDTO criarTaskDTO) {
        Task tarefaAtualizada = taskService.atualizarTarefa(criarTaskDTO, id);
        return ResponseEntity.ok(tarefaAtualizada);
        }
    }

