package devmarcos.todolist.repository;

import devmarcos.todolist.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Override
    Optional<Task> findById(Long id);

    Task save(Task task);

    @Override
    void deleteById(Long id);


}
