package backend.academy.linktracker.scrapper.model.linkdto;

import backend.academy.linktracker.scrapper.model.LinkType;
import java.time.Instant;
import java.util.List;


public record Link(long id, LinkType type, String url, List<String> tags, Instant lastCheck) {
    public Link(Link link, Instant timeLastCheck) {
        this(link.id, link.type, link.url, link.tags, timeLastCheck);
    }
}
