package devmarcos.todolist.controller;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    TaskService taskService;


    //    metodo de adicioanr algo no bd sempre vai ser POST pra responder a requisição
    @PostMapping
    @ResponseBody
    public ResponseEntity<String> CadastrarTarefa(@RequestBody CriarTaskDTO criarTaskDTO) {


        if (criarTaskDTO.descricao() != null && criarTaskDTO.status() != null) {

            var tarefaSalva = taskService.CriarTarefa(criarTaskDTO);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/tasks")
                    .buildAndExpand(tarefaSalva)
                    .toUri();
            return ResponseEntity.created(location).body(tarefaSalva);

        }else  {
            return ResponseEntity.badRequest().body("Ocorreu um erro ao cadastrar task" +
                    "Algum dos elementos não foi preenchido.");
        }

    }

    @GetMapping("/{idtarefa}")
    public ResponseEntity<Task> listarTarefa(@PathVariable("idtarefa") Long idtarefa) {
        var taskConsultada = taskService.SelecionarTarefa(idtarefa);

        if(taskConsultada != null) {
            return ResponseEntity.ok(taskConsultada);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAll() {
        List<Task> consulta = taskService.ConsultarTodas();
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> ExcluirTarefa(@PathVariable("id") Long id) {
        var delete = taskService.DeletarTarefa(id);

        if(delete != null) {
            return ResponseEntity.ok(delete);
        } else  {
            return ResponseEntity.notFound().build();
        }

    }
    @PutMapping("/{id}/")
    public ResponseEntity<String> AtualizarTarefa(@PathVariable("id") Long id, @RequestBody CriarTaskDTO criarTaskDTO) {
        var tarefa =  taskService.AtualizarTarefa(criarTaskDTO, id);

        return ResponseEntity.ok(tarefa);
    }
}
