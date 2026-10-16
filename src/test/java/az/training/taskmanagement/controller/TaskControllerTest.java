package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web layer testi (@WebMvcTest).
 *
 * Yalnız controller + JSON + validation + GlobalExceptionHandler yüklənir;
 * Service @MockBean ilə əvəz olunur (real database qalxmır).
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    @DisplayName("GET /tasks/1 → 200 və düzgün body")
    void getById_returns200() throws Exception {
        TaskResponse resp = new TaskResponse(1L, "Task", "desc",
                TaskStatus.TODO, Priority.HIGH, 1L, null, null);
        when(taskService.getTaskById(1L)).thenReturn(resp);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    @DisplayName("POST /tasks (düzgün) → 201")
    void create_returns201() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Task", "desc", Priority.HIGH, 1L);
        TaskResponse resp = new TaskResponse(5L, "Task", "desc",
                TaskStatus.TODO, Priority.HIGH, 1L, null, null);
        when(taskService.createTask(any())).thenReturn(resp);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    @DisplayName("POST /tasks (title boş) → 400")
    void create_invalidBody_returns400() throws Exception {
        // title və userId yoxdur → @Valid uğursuz olur
        String invalidJson = "{\"description\":\"only description\"}";

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /tasks/99 (yoxdur) → 404")
    void getById_missing_returns404() throws Exception {
        when(taskService.getTaskById(99L))
                .thenThrow(ResourceNotFoundException.of("Task", 99L));

        mockMvc.perform(get("/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /tasks/1 → 204")
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
