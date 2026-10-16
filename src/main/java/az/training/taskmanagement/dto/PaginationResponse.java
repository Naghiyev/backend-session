package az.training.taskmanagement.dto;

import java.util.List;

/**
 * Səhifələnmiş (paginated) cavab.
 *
 * @param content
 * @param page
 * @param size
 * @param totalElements
 * @param totalPages
 */
public record PaginationResponse<T>(List<T> content,
                              int page,
                              int size,
                              long totalElements,
                              int totalPages) {
}