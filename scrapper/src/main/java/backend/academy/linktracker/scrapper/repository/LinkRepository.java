package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.domain.Link;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    List<Link> findLinksFilteredByDelayTime(long lastCheckId, long linksLimit, Instant delayTime);

    Link save(Link link);

    List<Link> findAllByIdIn(List<Long> linksId);

    void updateLastCheckForLink(List<Link> link);

    void removeByUrl(String url);

    Optional<Link> findByUrl(String url);
}
