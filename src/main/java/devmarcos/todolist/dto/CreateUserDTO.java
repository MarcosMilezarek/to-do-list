package devmarcos.todolist.dto;

import java.time.Instant;


public record CreateUserDTO(String name, String email, Instant creationTime, Instant modificationTime) {

}
