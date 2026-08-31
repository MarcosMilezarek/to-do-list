package devmarcos.todolist.repository;

import devmarcos.todolist.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t JOIN FETCH t.usuario WHERE t.usuario.id = :usuarioId")
    List<Task> consultarTodasDoUsuario(@Param("usuarioId") Long usuarioId);
}
