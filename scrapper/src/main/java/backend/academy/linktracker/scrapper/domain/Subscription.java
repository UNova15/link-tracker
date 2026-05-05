package backend.academy.linktracker.scrapper.domain;

import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Subscription {
    private final long chatId;
    private final long linkId;

    @Getter(AccessLevel.NONE)
    private final List<String> tags;

    public static Subscription createNew(long chatId, long linkId, List<String> tags) {
        if (chatId < 0 || linkId < 0 || tags == null) {
            throw new IllegalArgumentException("chat id and link id must be positive");
        }
        List<String> tagsCopy = List.copyOf(tags);
        return new Subscription(chatId, linkId, tagsCopy);
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }
}
