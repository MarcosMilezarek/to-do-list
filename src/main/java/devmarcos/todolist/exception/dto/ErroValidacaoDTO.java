package devmarcos.todolist.exception.dto;

import org.springframework.http.HttpStatus;
import java.time.Instant;
import java.util.Map;

public record ErroValidacaoDTO(String path, String message, Instant time, HttpStatus status, Map<String, String> erros) {}
