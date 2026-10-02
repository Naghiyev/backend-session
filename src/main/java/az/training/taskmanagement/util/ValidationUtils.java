package az.training.taskmanagement.util;

import az.training.taskmanagement.exception.ValidationException;

import java.util.Locale;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private ValidationUtils() {
    }

    public static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(field, "boş ola bilməz");
        }
        return value.trim();
    }

    public static String requireNonBlank(String value, String field, int maxLength) {
        String trimmed = requireNonBlank(value, field);
        if (trimmed.length() > maxLength) {
            throw new ValidationException(field, "maksimum " + maxLength + " simvol ola bilər");
        }
        return trimmed;
    }

    public static <T> T requireNonNull(T value, String field) {
        if (value == null) {
            throw new ValidationException(field, "null ola bilməz");
        }
        return value;
    }

    public static String requireValidEmail(String email) {
        String normalized = requireNonBlank(email, "email").toLowerCase(Locale.ROOT);
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new ValidationException("email", "format yanlışdır: " + email);
        }
        return normalized;
    }
}

