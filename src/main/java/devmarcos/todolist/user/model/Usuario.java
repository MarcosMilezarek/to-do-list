package devmarcos.todolist.user.model;

import devmarcos.todolist.task.model.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class Usuario implements UserDetails {
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
