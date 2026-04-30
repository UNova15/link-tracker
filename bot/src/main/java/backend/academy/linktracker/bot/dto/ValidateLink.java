package backend.academy.linktracker.bot.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ValidateLinkResponse(
    @NotBlank(message = "Ссылка не должна быть пустой")
    @URL(message = "Некорректный формат ссылки")
    String url
) {
}
