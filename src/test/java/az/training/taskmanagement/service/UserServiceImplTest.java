package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.UserResponse;
import az.training.taskmanagement.exception.DuplicateResourceException;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserService üçün unit testlər.
 *
 * Repository @Mock ilə əvəz olunur - database olmadan yalnız business logic test edilir.
 * Struktur: Arrange (hazırla) - Act (çağır) - Assert (yoxla).
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("createUser: düzgün input → user yaradılır")
    void createUser_success() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("Darya", "darya@example.com");
        when(userRepository.existsByEmail("darya@example.com")).thenReturn(false);
        User saved = new User("Darya", "darya@example.com");
        saved.setId(1L);
        when(userRepository.save(any())).thenReturn(saved);

        // Act
        UserResponse response = userService.createUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id().longValue());
        assertEquals("darya@example.com", response.email());
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("createUser: təkrar email → DuplicateResourceException")
    void createUser_duplicateEmail_throws() {
        CreateUserRequest request = new CreateUserRequest("Darya", "darya@example.com");
        when(userRepository.existsByEmail("darya@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("createUser: boş name → ValidationException")
    void createUser_blankName_throws() {
        CreateUserRequest request = new CreateUserRequest("  ", "darya@example.com");
        assertThrows(ValidationException.class, () -> userService.createUser(request));
    }

    @Test
    @DisplayName("getUserById: mövcud deyil → ResourceNotFoundException")
    void getUserById_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    @DisplayName("deleteUser: mövcud user → deleteById çağırılır")
    void deleteUser_success() {
        User user = new User("Darya", "darya@example.com");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
