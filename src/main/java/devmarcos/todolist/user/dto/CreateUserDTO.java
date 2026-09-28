package devmarcos.todolist.user.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotBlank(message = "Email é obrigatório") String email,
        @NotBlank(message = "Senha é obrigatória") String senha
    ) {

}
