package devmarcos.todolist.dto;

import devmarcos.todolist.Model.Task;

import java.time.Instant;


public record TaskResponseDTO(
        Long id, String descricao, String status,
        Instant creationTime, Instant modificationTime,
        UsuarioResumoDTO usuario, CategoriaResumoDTO categoria
) {
    public static TaskResponseDTO from(Task task) {
        UsuarioResumoDTO usuarioDTO = task.getUsuario() != null
                ? new UsuarioResumoDTO(task.getUsuario().getId(), task.getUsuario().getNome())
                : null;

        CategoriaResumoDTO categoriaDTO = task.getCategoria() != null
                ? new CategoriaResumoDTO(task.getCategoria().getId(), task.getCategoria().getNome())
                : null;

        return new TaskResponseDTO(
                task.getId(),
                task.getDescricao(),
                task.getStatus(),
                task.getCreationTime(),
                task.getModificationTime(),
                usuarioDTO,
                categoriaDTO
        );
    }
}
