package backend.academy.linktracker.scrapper.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Link {
    private Long id;
    private LinkType type;
    private String url;
    //время последней проверки шедулером
    private Instant lastCheck;
    //время последнего изменения по отслеживаемой ссылки
    private Instant lastUpdate;

    public static Link createNew(LinkType type, String url) {
        if (type == null || url == null || url.isBlank()) {
            throw new IllegalArgumentException("Incorrect data to create link");
        }

        Instant createdAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
        return new Link(null, type, url, createdAt, null);
    }

    public static Link restore(long id, LinkType type, String url, Instant lastCheck, Instant lastUpdate) {
        if (id < 0
            || type == null
            || url == null
            || url.isBlank()
            || lastCheck == null
            || lastCheck.isAfter(Instant.now())) {
            throw new IllegalArgumentException("Incorrect data to create link");
        }
        return new Link(id, type, url, lastCheck, lastUpdate);
    }

    public void markCheckedNow() {
        this.lastCheck = Instant.now().truncatedTo(ChronoUnit.MICROS);
    }

    public void updateLastUpdateTime(Instant updateTime) {

        if (updateTime == null || (lastUpdate != null && lastUpdate.isAfter(updateTime))) {
            throw new IllegalArgumentException("Incorrect updated time");
        }
        this.lastUpdate = updateTime;
    }
}
