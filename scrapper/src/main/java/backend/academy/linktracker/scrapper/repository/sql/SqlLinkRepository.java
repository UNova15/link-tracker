package backend.academy.linktracker.scrapper.repository.sql;

import backend.academy.linktracker.scrapper.domain.DBRecordStatus;
import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.sql.dao.LinkDao;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "app", name = "db-provider", havingValue = "sql")
@AllArgsConstructor
public class SqlLinkRepository implements LinkRepository {
    private final LinkDao linkDao;

    @Override
    public List<Link> findLinksFilteredByLastCheck(long lastCheckId, long linksLimit, Instant lastCheck) {
        return linkDao.findLinksFilteredByLastCheck(lastCheckId, linksLimit, lastCheck);
    }

    @Override
    public Link save(Link link) {
        return linkDao.save(link);
    }

    @Override
    public List<Link> findAllByIdIn(List<Long> linksId) {
        return linkDao.findAllByIdIn(linksId);
    }

    @Override
    public void updateAndMarkAsIdle(List<Link> link) {
        linkDao.updateLastCheckAndLastUpdate(link);
    }

    @Override
    public void removeByUrl(String url) {
        linkDao.removeByUrl(url);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return linkDao.findByUrl(url);
    }

    @Override
    public void markAsProcessing(List<Long> ids) {
        linkDao.updateStatus(ids, DBRecordStatus.PROCESSING);
    }
}
