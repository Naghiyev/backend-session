package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);
    
    boolean existsByUserId(Long userId);

    @Modifying
    @Query("UPDATE Task t SET t.user = null WHERE t.user.id = :userId")
    int unassignUser(@Param("userId") Long userId);

    List<Task> findByCategoryId(Long categoryId);

    @Modifying
    @Query("UPDATE Task t SET t.category = null WHERE t.category.id = :categoryId")
    int unassignCategory(@Param("categoryId") Long categoryId);
}
