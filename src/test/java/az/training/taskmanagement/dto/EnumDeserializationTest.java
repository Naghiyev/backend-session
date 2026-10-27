package az.training.taskmanagement.dto;

import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class EnumDeserializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void unknownPriority_isRejected() {
        String json = """
                { "title": "Test", "priority": "URGENT", "userId": 1 }
                """;

        assertThatThrownBy(() -> objectMapper.readValue(json, CreateTaskRequest.class))
                .isInstanceOf(InvalidFormatException.class)
                .hasMessageContaining("URGENT");
    }

    @Test
    void lowercasePriority_isRejected() {
        String json = """
                { "title": "Test", "priority": "high", "userId": 1 }
                """;

        assertThatThrownBy(() -> objectMapper.readValue(json, CreateTaskRequest.class))
                .isInstanceOf(InvalidFormatException.class);
    }

    @Test
    void unknownStatus_isRejected() {
        String json = """
                { "status": "FINISHED" }
                """;

        assertThatThrownBy(() -> objectMapper.readValue(json, UpdateTaskRequest.class))
                .isInstanceOf(InvalidFormatException.class)
                .hasMessageContaining("FINISHED");
    }

    @Test
    void validValues_areAccepted() throws Exception {
        CreateTaskRequest create = objectMapper.readValue(
                "{ \"title\": \"Test\", \"priority\": \"HIGH\", \"userId\": 1 }", CreateTaskRequest.class);
        UpdateTaskRequest update = objectMapper.readValue(
                "{ \"status\": \"DONE\" }", UpdateTaskRequest.class);

        assertThat(create.priority()).isEqualTo(Priority.HIGH);
        assertThat(update.status()).isEqualTo(TaskStatus.DONE);
    }
}
