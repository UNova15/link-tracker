package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.exception.LinkNotFoundException;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.model.linkdto.Link;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

//In-memory имитация базы данных
@Repository
public class LinkRepository {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Set<Link> repository = new HashSet<>();

    public Set<Link> getAllLinks() {
        return repository;
    }

    public Link saveLink(LinkType type, String url, String[] tags, Instant lastChackTime) {
        long id = idGenerator.getAndIncrement();
        Link link = new Link(id, type, url, tags, lastChackTime);

        repository.add(link);
        return link;
    }

    public List<Link> findLinksByLinksId(List<Long> linksId) {
        List<Link> links = new ArrayList<>();

        for (long linkId : linksId) {
            for (Link link : repository) {
                if (link.id() == linkId) {
                    links.add(link);
                    break;
                }
            }
        }

        return links;
    }

    public List<Link> findLinkByLinkId(long linkId) {
        return repository.stream()
            .filter(link -> link.id() == linkId)
            .toList();
    }

    //TODO исключения в другой слой перенести
    public Link removeLink(String url) {
        Link link = findLinkByUrl(url).orElseThrow(() -> new LinkNotFoundException(url));
        repository.remove(link);
        return link;
    }

    public Optional<Link> findLinkByUrl(String url) {
        return repository.stream()
            .filter(link -> link.url().equals(url))
            .findAny();
    }

    public boolean exists(String url) {
        return findLinkByUrl(url).isPresent();
    }
}
