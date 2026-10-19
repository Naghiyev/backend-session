package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    // Metodun ADINDAN sorğu yaradılır:
    // SELECT COUNT(*) > 0 FROM users WHERE email = ?
    boolean existsByEmail(String email);
}