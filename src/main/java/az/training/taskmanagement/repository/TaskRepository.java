package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t from Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%' ,:keyword, '%')) ")
            List<Task> searchByTitle(@Param("keyword") String keyword);

    // user.id üzrə axtarış (nested property → user_ id)
    List<Task> findByUser_Id(Long userId);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByUser_IdAndStatus(Long userId, TaskStatus status);
}
