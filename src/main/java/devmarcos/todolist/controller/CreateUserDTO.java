package devmarcos.todolist.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;

import java.time.Instant;


public record CreateUserDTO(String name, String email, Instant creationTime, Instant modificationTime) {

}
