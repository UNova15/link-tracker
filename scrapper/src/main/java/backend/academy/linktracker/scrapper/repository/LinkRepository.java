package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.domain.Link;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    List<Link> findLinksFilteredByLastCheck(long lastCheckId, long linksLimit, Instant lastCheck);

    Link save(Link link);

    List<Link> findAllByIdIn(List<Long> linksId);

    void updateAndMarkAsIdle(List<Link> link);

    void removeByUrl(String url);

    Optional<Link> findByUrl(String url);

    void markAsProcessing(List<Long> links);
}
