package devmarcos.todolist.exception;

public class UsuarioNaoEncontrado extends RuntimeException {
    public UsuarioNaoEncontrado(Long id) {
        super("Usuário com id " + id + " não encontrado.");
    }

    public UsuarioNaoEncontrado(String email) {
        super("Usuário com email " + email + " não encontrado.");
    }

}
