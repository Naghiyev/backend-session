package az.training.taskmanagement.dto;

import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;

/**
 * Task-ı qismən yeniləmək üçün (PATCH). Bütün sahələr nullable-dır:
 * yalnız dəyəri verilən sahələr yenilənir.
 */
public record UpdateUserRequest(String name, String email) {
}
