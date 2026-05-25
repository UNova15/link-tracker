package backend.academy.linktracker.bot.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {
    @NotNull
    private final UUID idempotencyKey;

    @PositiveOrZero
    private final long linkId;

    @NotBlank
    @URL
    private final String url;

    @NotBlank
    private final String description;

    @NotEmpty
    private final List<Long> tgChatIds;

    public static Notification createNotification(
            UUID idempotencyKey, long linkId, String url, String description, List<Long> tgChatIds) {
        if (idempotencyKey == null
                || linkId < 0
                || url == null
                || url.isBlank()
                || description == null
                || description.isBlank()
                || tgChatIds == null
                || tgChatIds.isEmpty()) {
            throw new IllegalArgumentException("Invalid data to create notification");
        }
        return new Notification(idempotencyKey, linkId, url, description, tgChatIds);
    }
}
