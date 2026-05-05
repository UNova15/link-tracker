package unit.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import backend.academy.linktracker.bot.validator.LinkValidator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LinkValidatorTest {

    private static LinkValidator linkValidator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        linkValidator = new LinkValidator(validator);
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "https://stackoverflow.com/questions/1234",
                "http://github.com/my-repo/issues",
                "https://www.google.com"
            })
    void validate_withValidUrl_returnEmpty(String url) {
        Optional<String> value = linkValidator.validate(url);

        assertTrue(value.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t\n"})
    void validate_withEmptyUrl_returnEmpty(String url) {
        String expectedMessage1 = "Ссылка не должна быть пустой";
        String expectedMessage2 = "Некорректный формат ссылки";

        Optional<String> value = linkValidator.validate(url);

        assertTrue(value.isPresent());
        assertTrue(expectedMessage1.equals(value.get()) || expectedMessage2.equals(value.get()));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"htps://stackoverflow.com/questions/1234", "hеtp:/github.com/my-repo/issues", "www.google.com"})
    void validate_withInvalidUrl_returnEmpty(String url) {
        String expectedString = "Некорректный формат ссылки";

        Optional<String> value = linkValidator.validate(url);

        assertTrue(value.isPresent());
        assertEquals(expectedString, value.get());
    }
}
