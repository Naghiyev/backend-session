package az.training.taskmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Bean Validation ilə server-side yoxlama.
 * @Valid controller-də tətbiq olunanda bu qaydalar avtomatik işləyir.
 */
public record CreateUserRequest(

        @NotBlank(message = "name boş ola bilməz")
        @Size(max = 100, message = "name maksimum 100 simvol")
        String name,

        @NotBlank(message = "email boş ola bilməz")
        @Email(message = "email formatı yanlışdır")
        String email
) {
}
