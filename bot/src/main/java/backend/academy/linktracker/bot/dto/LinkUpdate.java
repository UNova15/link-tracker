package backend.academy.linktracker.bot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.URL;
import java.util.List;

public record LinkUpdate(
        @PositiveOrZero long id,
        @NotBlank @URL String url,
        @NotBlank String description,
        @NotEmpty List<Long> tgChatIds) {}
