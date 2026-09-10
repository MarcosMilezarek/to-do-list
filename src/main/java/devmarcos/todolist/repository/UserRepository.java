package devmarcos.todolist.repository;

import devmarcos.todolist.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT t FROM Usuario t WHERE t.email = :email")
    Usuario findByEmail(@Param("email") String email);
}
