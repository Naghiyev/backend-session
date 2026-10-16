package az.training.taskmanagement.dto;

import az.training.taskmanagement.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(

        @NotBlank(message = "title boş ola bilməz")
        @Size(max = 200, message = "title maksimum 200 simvol")
        String title,

        @Size(max = 1000, message = "description maksimum 1000 simvol")
        String description,

        Priority priority,

        @NotNull(message = "userId tələb olunur")
        Long userId
) {
}
