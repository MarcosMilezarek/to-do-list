package devmarcos.todolist.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
public class Usuario{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;

    @CreationTimestamp
    private Instant creationTime;

    @UpdateTimestamp
    private Instant modificationTime;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Task> tasks;

    public Usuario(String nome, String email, Instant creationTime, Instant modificationTime, String senha) {
        this.nome = nome;
        this.email = email;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
        this.senha = senha;
    }

    public Usuario() {
    }

    public Usuario(Long id) {
        this.id = id;
    }

}