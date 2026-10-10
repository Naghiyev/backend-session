package az.training.taskmanagement.repository.inMemory;

import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory User repository.
 *
 * Lesson 1: verilənlər hələ database-də deyil, yaddaşda (Map) saxlanılır.
 * id generasiyası AtomicLong ilə edilir.

 * Sonrakı dərslərdə bu sinif əvvəlcə interface-ə çevriləcək (Lesson 2),
 * daha sonra Spring Data JPA repository ilə əvəz olunacaq (Lesson 5).
 */
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(sequence.incrementAndGet());
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }
    @Override
    public boolean existsByEmail(String email) {
        return storage.values().stream()
                .anyMatch(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email));
    }
    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
