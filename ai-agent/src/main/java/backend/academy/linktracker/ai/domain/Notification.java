package backend.academy.linktracker.ai.domain;

import lombok.With;
import java.util.UUID;

public record Notification(
        UUID idempotenceKey,
        long linkId,
        String url,
        String author,
        long tgChatId,
        @With String description,
        @With Priority priority) {

}
