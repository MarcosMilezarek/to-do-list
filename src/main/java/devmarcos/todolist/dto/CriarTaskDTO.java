package devmarcos.todolist.dto;
import jakarta.validation.constraints.NotBlank;

public record CriarTaskDTO(
        @NotBlank(message = "Descrição é obrigatória") String descricao,
        @NotBlank(message = "Status é obrigatório") String status,
        Long id_user) {

}
