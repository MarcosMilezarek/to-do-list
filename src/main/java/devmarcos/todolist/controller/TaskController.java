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
    TaskService gerenciatask;

    @PostMapping("/novatarefa/")
    @ResponseBody
    public ResponseEntity<String> CadastrarTarefa(@RequestBody CriarTaskDTO criarTaskDTO) {
        var tarefaSalva = gerenciatask.CriarTarefa(criarTaskDTO);
        return  ResponseEntity.ok(tarefaSalva);
    };

    @GetMapping("/listarTarefa/{id}")
    public ResponseEntity<List<Task>> listarTarefa(@PathVariable("id")Integer idtarefa, @RequestBody Task tarefa) {
        return ResponseEntity.ok(gerenciatask.SelecionarTarefa(idtarefa));
    }




}
