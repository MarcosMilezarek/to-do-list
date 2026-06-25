package devmarcos.todolist.Model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Task {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descricao;
    private String status;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private LocalDateTime data_limite;

    public void setId(Integer id) {
        this.id = id;
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

    @GeneratedValue(strategy = GenerationType.AUTO)
    public LocalDateTime getData_limite() {
        return data_limite;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    public void setData_limite(LocalDateTime data_limite) {
        this.data_limite = data_limite;
    }

    public Task(Integer id, String descricao, String status, LocalDateTime data_limite) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.data_limite = data_limite;
    }
}
