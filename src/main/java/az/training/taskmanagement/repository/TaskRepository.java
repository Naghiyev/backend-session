package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // user.id üzrə axtarış (nested property → user_ id)
    List<Task> findByUser_Id(Long userId);

    List<Task> findByStatus(TaskStatus status);

    @Query("""
            SELECT t
            FROM Task t
            WHERE t.priority= :priority 
            """
    )
    List<Task> findTaskByPriority(@Param("priority") Priority priority);


    List<Task> findByUser_IdAndStatus(Long userId, TaskStatus status);
}
