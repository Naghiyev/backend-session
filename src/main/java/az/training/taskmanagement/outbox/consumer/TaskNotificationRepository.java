package az.training.taskmanagement.outbox.consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskNotificationRepository extends JpaRepository<TaskNotification, Long> {

    List<TaskNotification> findTop50ByOrderByCreatedAtDesc();
}
