package devmarcos.todolist.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @OneToMany(mappedBy = "categoria")
    private List<Task> tasks;

    public Category(String nome) {
        this.nome = nome;
    }

    public Category() {

    }
}
