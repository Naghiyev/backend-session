package az.training.taskmanagement.dto;

import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;
import jakarta.validation.constraints.Size;

/**
 * PATCH - bütün sahələr optional. Verilən sahə üçün yalnız ölçü yoxlanır.
 */
public record UpdateTaskRequest(

        @Size(max = 200, message = "title maksimum 200 simvol")
        String title,

        @Size(max = 1000, message = "description maksimum 1000 simvol")
        String description,

        TaskStatus status,

        Priority priority
) {
}
