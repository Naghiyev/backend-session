package az.training.taskmanagement.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> Users,
        int page,
        int size,
        long allElements,
        int allPages
) {

}
