package backend.academy.linktracker.scrapper.domain;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Link {
    private Long id;
    private LinkType type;
    private String url;
    private Instant lastCheck;

    public static Link createNew(LinkType type, String url) {
        if (type == null || url == null || url.isBlank()) {
            throw new IllegalArgumentException("Incorrect data to create link");
        }
        return new Link(null, type, url, Instant.now());
    }

    public static Link restore(long id, LinkType type, String url, Instant lastCheck) {
        if (id < 0
                || type == null
                || url == null
                || url.isBlank()
                || lastCheck == null
                || lastCheck.isAfter(Instant.now())) {
            throw new IllegalArgumentException("Incorrect data to create link");
        }
        return new Link(id, type, url, lastCheck);
    }

    public void markCheckedNow() {
        this.lastCheck = Instant.now();
    }
}
