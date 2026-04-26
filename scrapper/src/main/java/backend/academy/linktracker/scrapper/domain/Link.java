package backend.academy.linktracker.scrapper.domain;

import java.time.Instant;

public record Link(long id, LinkType type, String url, Instant lastCheck) {
    public Link(Link link, Instant timeLastCheck) {
        this(link.id, link.type, link.url, timeLastCheck);
    }
}
