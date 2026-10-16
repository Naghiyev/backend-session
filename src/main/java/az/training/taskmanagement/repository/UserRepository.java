package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository.
 *
 * JpaRepository bizə hazır CRUD verir (save, findById, findAll, deleteById...).
 * "existsByEmail" isə **derived query**-dir: Spring metod adından SQL yaradır.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
