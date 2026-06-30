package devmarcos.todolist.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "Tarefas")
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

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
