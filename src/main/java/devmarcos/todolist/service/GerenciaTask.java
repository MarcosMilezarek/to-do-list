package devmarcos.todolist.service;

import devmarcos.todolist.Model.Task;

import java.util.List;

public interface GerenciaTask {

    String CriarTarefa(Task task);

    String AtualizarTarefa(Task task);

    String DeletarTarefa(Task task);

    List<Task> SelecionarTarefa(Integer id);

}
