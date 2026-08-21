package devmarcos.todolist.exception;

public class TaskNaoEncontradaException extends RuntimeException {
    public TaskNaoEncontradaException(Long id) {
        super("Tarefa com id " + id + " não encontrada.");
    }
}
