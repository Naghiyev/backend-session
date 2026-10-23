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
        @Size(min = 2, max = 20, message = "name 2 ilə 20 simvol arasinda olmalidir")
        String name,

        @NotBlank(message = "email boş ola bilməz")
        @Email(message = "email formatı yanlışdır")
        String email
) {
}
