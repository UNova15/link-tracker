package backend.academy.linktracker.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.UUID;

public record NotificationDto(
        @NotNull UUID idempotence_key,
        @PositiveOrZero long link_id,
        @NotBlank String url,
        @NotBlank String author,
        @NotBlank String description,
        @NotEmpty List<Long> tgChatIds) {}
