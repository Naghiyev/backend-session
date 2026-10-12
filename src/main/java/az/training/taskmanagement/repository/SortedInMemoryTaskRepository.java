package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Primary
public class SortedInMemoryTaskRepository implements TaskRepository {

    private final List<Task> tasks = new ArrayList<>();
    private long idCounter = 1;

    @Override
    public <S extends Task> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(idCounter++);
            tasks.add(entity);
        } else {
            tasks.removeIf(t -> t.getId().equals(entity.getId()));
            tasks.add(entity);
        }
        return entity;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    @Override
    public List<Task> findAll() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByUser_Id(Long userId) {
        return tasks.stream()
                .filter(t -> t.getUser() != null && t.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return tasks.stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        tasks.removeIf(t -> t.getId().equals(id));
    }


    @Override public void flush() {}
    @Override public <S extends Task> S saveAndFlush(S entity) { return save(entity); }
    @Override public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) { return null; }
    @Override public void deleteAllInBatch(Iterable<Task> entities) {}
    @Override public void deleteAllByIdInBatch(Iterable<Long> longs) {}
    @Override public void deleteAllInBatch() {}
    @Override public Task getOne(Long id) { return findById(id).orElse(null); }
    @Override public Task getById(Long id) { return findById(id).orElse(null); }
    @Override public Task getReferenceById(Long id) { return findById(id).orElse(null); }
    @Override public <S extends Task> List<S> findAll(Example<S> example) { return null; }
    @Override public <S extends Task> List<S> findAll(Example<S> example, Sort sort) { return null; }
    @Override public <S extends Task> List<S> saveAll(Iterable<S> entities) { return null; }
    @Override public List<Task> findAllById(Iterable<Long> longs) { return null; }
    @Override public long count() { return tasks.size(); }
    @Override public void delete(Task entity) { deleteById(entity.getId()); }
    @Override public void deleteAllById(Iterable<? extends Long> longs) {}
    @Override public void deleteAll(Iterable<? extends Task> entities) {}
    @Override public void deleteAll() { tasks.clear(); }
    @Override public boolean existsById(Long id) { return findById(id).isPresent(); }
    @Override public List<Task> findAll(Sort sort) { return findAll(); }
    @Override public Page<Task> findAll(Pageable pageable) { return null; }
    @Override public <S extends Task> Optional<S> findOne(Example<S> example) { return Optional.empty(); }
    @Override public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) { return null; }
    @Override public <S extends Task> long count(Example<S> example) { return 0; }
    @Override public <S extends Task> boolean exists(Example<S> example) { return false; }
    @Override public <S extends Task, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) { return null; }
}