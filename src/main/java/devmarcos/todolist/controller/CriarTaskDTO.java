package devmarcos.todolist.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

public record CriarTaskDTO(String descricao, String status) {
}
