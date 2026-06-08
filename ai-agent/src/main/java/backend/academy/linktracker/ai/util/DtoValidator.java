package backend.academy.linktracker.ai.util;

import backend.academy.linktracker.ai.dto.NotificationDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class DtoValidator {
    private final Validator validator;

    public boolean isValid(NotificationDto dto) {
        Set<ConstraintViolation<NotificationDto>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            log.error("Ошибка валидации notification dto: {}", violations);
            return false;
        }
        return true;
    }
}
