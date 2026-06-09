package backend.academy.linktracker.ai.domain;

import java.util.UUID;
import lombok.With;

public record Notification(
        UUID idempotenceKey,
        long linkId,
        String url,
        String author,
        long tgChatId,
        @With String description,
        @With Priority priority) {}
