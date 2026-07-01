package devmarcos.todolist.controller;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;


    //    metodo de adicioanr algo no bd sempre vai ser POST pra responder a requisição
    @PostMapping("/novatarefa/")
    @ResponseBody
    public ResponseEntity<String> CadastrarTarefa(@RequestBody CriarTaskDTO criarTaskDTO) {
        var tarefaSalva = taskService.CriarTarefa(criarTaskDTO);
        return ResponseEntity.ok(tarefaSalva);
    }

    @GetMapping("/listarTarefa/{idtarefa}")
    public ResponseEntity<Task> listarTarefa(@PathVariable("idtarefa") Long idtarefa) {
        var taskConsultada = taskService.SelecionarTarefa(idtarefa);

        if(taskConsultada != null) {
            return ResponseEntity.ok(taskConsultada);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Task>> getAll() {
        List<Task> consulta = taskService.ConsultarTodas();
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<String> ExcluirTarefa(@PathVariable("id") Long id) {
        var delete = taskService.DeletarTarefa(id);

        return ResponseEntity.ok(delete);
    }
    @PutMapping("/update/{id}/")
    public ResponseEntity<String> AtualizarTarefa(@PathVariable("id") Long id, @RequestBody CriarTaskDTO criarTaskDTO) {
        var tarefa =  taskService.AtualizarTarefa(criarTaskDTO, id);

        return ResponseEntity.ok(tarefa);
    }
}
