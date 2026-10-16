package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository.
 *
 * JpaRepository bizə hazır CRUD verir (save, findById, findAll, deleteById...).
 * "existsByEmail" isə **derived query**-dir: Spring metod adından SQL yaradır.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Page<User> findByName(String name, Pageable pageable);
}
