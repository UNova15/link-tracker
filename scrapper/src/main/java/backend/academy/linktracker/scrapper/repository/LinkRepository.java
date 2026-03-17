package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.model.linkdto.Link;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

// In-memory имитация базы данных
@Repository
public class LinkRepository {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Map<String, Link> repository = new ConcurrentHashMap<>();

    public Collection<Link> getAllLinks() {
        return repository.values();
    }

    public Link saveLink(LinkType type, String url, Instant lastChackTime) {
        long id = idGenerator.getAndIncrement();
        Link link = new Link(id, type, url, lastChackTime);

        repository.put(url, link);
        return link;
    }

    public List<Link> findLinksByLinksId(List<Long> linksId) {
        List<Link> links = new ArrayList<>();

        for (long linkId : linksId) {
            for (Link link : repository.values()) {
                if (link.id() == linkId) {
                    links.add(link);
                    break;
                }
            }
        }

        return links;
    }

    public void updateLink(Link link) {
        repository.put(link.url(), link);
    }

    public void removeLink(String url) {
        repository.remove(url);
    }

    public Optional<Link> findLinkByUrl(String url) {
        return Optional.ofNullable(repository.get(url));
    }
}
