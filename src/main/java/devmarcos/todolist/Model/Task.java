package devmarcos.todolist.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "Tarefas")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private String status;

    @CreationTimestamp
    private Instant creationTime;
    @UpdateTimestamp
    private Instant modificationTime;

    public Task() {
    }

    public Task(String descricao, String status, Instant creationTime, Instant modificationTime) {
        this.descricao = descricao;
        this.status = status;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
    }
}
