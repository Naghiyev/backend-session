package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository abstraction.
 *
 * Lesson 2: service artıq konkret saxlamadan asılı deyil, yalnız bu
 * interface-dən asılıdır (Dependency Inversion Principle - SOLID-in "D"-si).
 * Lesson 5-də bu interface Spring Data JPA ilə əvəz olunacaq.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
