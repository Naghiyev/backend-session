package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository abstraction.
 *
 * Lesson 2: service artıq konkret saxlamadan asılı deyil, yalnız bu
 * interface-dən asılıdır (Dependency Inversion Principle - SOLID-in "D"-si).
 * Lesson 5-də bu interface Spring Data JPA ilə əvəz olunacaq.
 */
@Repository
public interface UserRepository  extends JpaRepository<User,Long> {

//    User save(User user);
//
//    Optional<User> findById(Long id);
//
//    List<User> findAll();
//
//    boolean existsByEmail(String email);
//
//    void deleteById(Long id);
}
