package backend.academy.linktracker.bot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.UUID;
import org.hibernate.validator.constraints.URL;

public record NotificationDto(
        @NotNull UUID idempotencyKey,

        @PositiveOrZero long linkId,

        @NotBlank @URL String url,

        @NotBlank String description,

        @NotEmpty List<Long> tgChatIds) {}
