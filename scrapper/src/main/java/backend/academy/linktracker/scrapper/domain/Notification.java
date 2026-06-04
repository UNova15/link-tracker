package backend.academy.linktracker.scrapper.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Notification {
    private final UUID idempotencyKey;
    private final long linkId;
    private final String url;
    private final String author;
    private final String description;
    private final List<Long> tgChatIds;

    public static Notification createNew(
            UUID idempotenceKey, long linkId, String url, String author, String description, List<Long> tgChatIds) {
        if (idempotenceKey != null
                && linkId >= 0
                && url != null
                && !url.isBlank()
                && description != null
                && !description.isBlank()
                && !tgChatIds.isEmpty()) {

            return new Notification(idempotenceKey, linkId, url, author, description, tgChatIds);
        } else {
            throw new IllegalArgumentException("Invalid data to create notification");
        }
    }

    @JsonCreator
    private Notification(
            @JsonProperty("idempotencyKey") UUID idempotencyKey,
            @JsonProperty("linkId") long linkId,
            @JsonProperty("url") String url,
            @JsonProperty("author") String author,
            @JsonProperty("description") String description,
            @JsonProperty("tgChatIds") List<Long> tgChatIds) {

        this.idempotencyKey = idempotencyKey;
        this.linkId = linkId;
        this.url = url;
        this.author = author;
        this.description = description;
        this.tgChatIds = tgChatIds;
    }
}
