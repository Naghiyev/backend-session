package az.training.taskmanagement.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class CreateUserRequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    // ---------- Tapşırıq 1: name @Size(min = 2, max = 50) ----------

    @Test
    void name_tooShort_isInvalid() {
        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(new CreateUserRequest("A", "a@test.com"));

        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .containsExactly("name");
    }

    @Test
    void name_tooLong_isInvalid() {
        String longName = "a".repeat(51);
        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(new CreateUserRequest(longName, "a@test.com"));

        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .containsExactly("name");
    }

    @Test
    void name_withinLimits_isValid() {
        assertThat(validator.validate(new CreateUserRequest("Ab", "ab@test.com"))).isEmpty();
        assertThat(validator.validate(new CreateUserRequest("a".repeat(50), "ab@test.com"))).isEmpty();
    }

    // ---------- Tapşırıq 3: yanlış email formatları ----------

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",        // @ yoxdur
            "@mail.com",           // @-dan əvvəl heç nə yoxdur
            "user@",               // domen yoxdur
            "user@@mail.com",      // iki @
            "user name@mail.com",  // boşluq
            "user@mail..com"       // ardıcıl iki nöqtə
    })
    void email_invalidFormats_areRejected(String email) {
        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(new CreateUserRequest("Turkay", email));

        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .containsExactly("email");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void email_blank_isRejected(String email) {
        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(new CreateUserRequest("Turkay", email));

        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("email boş ola bilməz");
    }

    @ParameterizedTest
    @ValueSource(strings = {"turkay@example.com", "t.novruzlu@mail.az", "user+tag@gmail.com"})
    void email_validFormats_areAccepted(String email) {
        assertThat(validator.validate(new CreateUserRequest("Turkay", email))).isEmpty();
    }

    @Test
    void email_withoutTopLevelDomain_isAcceptedByEmailAnnotation() {
        // Diqqət: @Email "user@localhost" kimi ünvanları da düzgün sayır (.com tələb etmir)
        assertThat(validator.validate(new CreateUserRequest("Turkay", "user@localhost"))).isEmpty();
    }
}