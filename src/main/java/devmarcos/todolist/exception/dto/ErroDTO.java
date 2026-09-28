package devmarcos.todolist.exception.dto;

import org.springframework.http.HttpStatus;
import java.time.Instant;

public record ErroDTO(String path, String message, Instant time, HttpStatus status) {
}
