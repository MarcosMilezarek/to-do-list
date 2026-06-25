package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;
import devmarcos.todolist.repository.TaskBD;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GerenciaTaskImpl implements GerenciaTask {
    private TaskBD taskBD;

    public GerenciaTaskImpl(TaskBD taskBD) {
        this.taskBD = taskBD;
    }

    @Override
    public String CriarTarefa(Task task) {
        taskBD.save(task);

        return "Tarefa Criada com sucesso!";
    }

    @Override
    public String AtualizarTarefa(Task task) {
           taskBD.save(task);
            return "Tarefa Atualizada com sucesso!";
    }

    @Override
    public String DeletarTarefa(Task task) {
        taskBD.delete(task);
        return "Tarefa removida com sucesso";
    }

    @Override
    public List<Task> SelecionarTarefa(Integer id) {

        return taskBD.findById(id).stream().toList();
    }
}
