package backend.academy.linktracker.bot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.UUID;

public record NotificationDto(
        @NotNull UUID idempotencyKey,

        @NotEmpty List<@PositiveOrZero Long> linksIds,

        @NotBlank String description,

        @PositiveOrZero long tgChatId) {}
