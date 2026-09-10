package devmarcos.todolist.dto;
import jakarta.validation.constraints.NotBlank;

public record CriarTaskDTO(
        @NotBlank String descricao,
        @NotBlank String status,
        Long id_user) {

}
