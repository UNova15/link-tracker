package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.domain.Link;
import java.util.List;
import java.util.Optional;


public interface LinkRepository {

    List<Link> getAll();

    Link save(Link link);

    List<Link> findAllByIdIn(List<Long> linksId);

    void update(Link link);

    void removeByUrl(String url);

    Optional<Link> findByUrl(String url);
}
