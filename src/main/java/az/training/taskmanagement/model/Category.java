package az.training.taskmanagement.model;

public record Category(Long id, String name, String description) {

    public Category withId(Long newId) {
        return new Category(newId, name, description);
    }
}