package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

//    Task save(Task task);
//
//    Optional<Task> findById(Long id);
//
//    List<Task> findAll();
//
//    List<Task> findByUserId(Long userId);
//
//    void deleteById(Long id);
}
