package devmarcos.todolist.controller;

import jakarta.validation.constraints.NotBlank;

public record CriarTaskDTO(
        @NotBlank(message = "Descrição não foi preenchida e é obrigatória") String descricao,
        @NotBlank(message = "Status não foi preenchido e é obrigatório")  String status) {

}
