package devmarcos.todolist.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErroDTO(String path, String message, Instant time, HttpStatus status) {
}
