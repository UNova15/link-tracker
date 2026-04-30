package backend.academy.linktracker.bot.validator;

import backend.academy.linktracker.bot.dto.ValidateLink;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class LinkValidator {
    private final Validator validator;

    public Optional<String> validate(String url) {
        ValidateLink link = new ValidateLink(url);
        Set<ConstraintViolation<ValidateLink>> violations = validator.validate(link);

        if (violations.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(violations.iterator().next().getMessage());
    }
}
