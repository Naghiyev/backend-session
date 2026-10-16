package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // user.id üzrə axtarış (nested property → user_ id)
    List<Task> findByUser_Id(Long userId);

    List<Task> findByStatus(TaskStatus status);
}
