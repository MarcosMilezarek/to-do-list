package devmarcos.todolist.controller;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.service.GerenciaTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskMenager {
    @Autowired
    GerenciaTask gerenciatask;

    @GetMapping("/listarTarefa/{id}")
    public ResponseEntity<List<Task>> listarTarefa(@RequestBody Integer idtarefa) {
        return ResponseEntity.ok(gerenciatask.SelecionarTarefa(idtarefa));
    }
}
