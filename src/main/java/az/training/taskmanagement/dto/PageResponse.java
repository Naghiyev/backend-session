package az.training.taskmanagement.dto;

import java.util.List;


public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public static <T> PageResponse<T> of(List<T> allItems, int page, int size) {
        int totalElements = allItems.size();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<T> pageContent = (fromIndex >= totalElements)
                ? List.of()
                : allItems.subList(fromIndex, toIndex);

        return new PageResponse<>(pageContent, page, size, totalElements, totalPages);
    }
}
