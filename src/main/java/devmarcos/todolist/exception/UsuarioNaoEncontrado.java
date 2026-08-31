package devmarcos.todolist.exception;

public class UsuarioNaoEncontrado extends RuntimeException {
    public UsuarioNaoEncontrado(Long id) {
        super("Usuário com id " + id + " não encontrada.");
    }

}
