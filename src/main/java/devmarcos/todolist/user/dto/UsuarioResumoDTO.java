package devmarcos.todolist.user.dto;

import devmarcos.todolist.user.model.Usuario;


public record UsuarioResumoDTO(Long id, String nome) {
    public static UsuarioResumoDTO from(Usuario usuario) {
        return new UsuarioResumoDTO(usuario.getId(), usuario.getNome());
    }
}
