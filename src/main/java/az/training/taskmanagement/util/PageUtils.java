package az.training.taskmanagement.util;

import az.training.taskmanagement.dto.PageResponse;
import az.training.taskmanagement.exception.ValidationException;

import java.util.List;

public final class PageUtils {

    public static final int MAX_SIZE = 100;

    private PageUtils() {
    }

    public static <T> PageResponse<T> paginate(List<T> all, int page, int size) {
        if (page < 0) {
            throw new ValidationException("page 0-dan kiçik ola bilməz");
        }
        if (size < 1 || size > MAX_SIZE) {
            throw new ValidationException("size 1 ilə " + MAX_SIZE + " arasında olmalıdır");
        }

        long total = all.size();
        int totalPages = (int) Math.ceil((double) total / size);
        
        int from = (int) Math.min((long) page * size, total);
        int to = (int) Math.min((long) from + size, total);

        List<T> content = List.copyOf(all.subList(from, to));
        return new PageResponse<>(content, page, size, total, totalPages,
                page == 0, page >= totalPages - 1);
    }
}