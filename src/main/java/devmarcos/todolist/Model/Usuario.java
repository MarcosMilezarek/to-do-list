package devmarcos.todolist.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String email;

    @CreationTimestamp
    private Instant creationTime;

    @UpdateTimestamp
    private Instant modificationTime;

    public Usuario(Integer id, String nome, String email, Instant creationTime, Instant modificationTime) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
    }

    public Usuario() {
    }

    public Integer getId() {
        return id;
    }

    public Usuario(Integer id) {
        this.id = id;
    }

}