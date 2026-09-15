package devmarcos.todolist.repository;

import devmarcos.todolist.Model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t JOIN FETCH t.usuario LEFT JOIN FETCH t.categoria " +
            "WHERE t.usuario.id = :usuarioId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:categoriaId IS NULL OR t.categoria.id = :categoriaId)")
    Page<Task> consultarTarefasPaginadaDoUsuario(@Param("usuarioId") Long usuarioId,
                                                 @Param("status") String status,
                                                 @Param("categoriaId") Long categoriaId,
                                                 Pageable pageable);
}
