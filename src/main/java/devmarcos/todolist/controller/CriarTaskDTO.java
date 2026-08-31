package devmarcos.todolist.controller;
import jakarta.validation.constraints.NotBlank;

public record CriarTaskDTO(
        @NotBlank String descricao,
        @NotBlank String status) {

}
