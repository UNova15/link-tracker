package backend.academy.linktracker.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.UUID;

public record NotificationDto(
        // TODO возможно нужно будет проверять уникальность ключей идемпотентности на этапе kafkastreams, а сами ключи
        //  хранить в valkey
        @NotNull UUID idempotenceKey,
        @PositiveOrZero long linkId,
        @NotBlank String url,
        @NotBlank String author,
        @NotBlank String description,
        @NotEmpty List<Long> tgChatIds) {}
