package devmarcos.todolist.dto;

import devmarcos.todolist.Model.Usuario;

public record UsuarioResumoDTO(Long id, String nome) {
    public static UsuarioResumoDTO from(Usuario usuario) {
        return new UsuarioResumoDTO(usuario.getId(), usuario.getNome());
    }
}
