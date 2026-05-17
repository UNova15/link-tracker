package backend.academy.linktracker.scrapper.repository.orm;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.mapper.LinkMapper;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.orm.entity.LinkEntity;
import backend.academy.linktracker.scrapper.repository.orm.jparepository.LinkJpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("orm")
@AllArgsConstructor
public class OrmLinkRepository implements LinkRepository {
    private final LinkJpaRepository repository;
    private final LinkMapper linkMapper;

    @Override
    public List<Link> findLinksFilteredByDelayTime(long lastCheckId, long linksLimit, Instant delayTime) {
        List<LinkEntity> links = repository.findLinksToCheckWithDelayTime(lastCheckId, linksLimit, delayTime);
        return linkMapper.fromListLinkEntity(links);
    }

    @Override
    public Link save(Link link) {
        LinkEntity linkEntity = LinkEntity.fromDomain(link);
        LinkEntity savedLink = repository.save(linkEntity);
        return linkMapper.fromLinkEntity(savedLink);
    }

    @Override
    public List<Link> findAllByIdIn(List<Long> linksId) {
        List<LinkEntity> linkEntities = repository.findAllById(linksId);
        return linkMapper.fromListLinkEntity(linkEntities);
    }

    @Override
    public void updateLastCheckAndLastUpdate(List<Link> links) {
        List<LinkEntity> linkEntities = linkMapper.toListOfLinkEntity(links);
        repository.saveAll(linkEntities);
    }

    @Override
    public void removeByUrl(String url) {
        repository.deleteByUrl(url);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        Optional<LinkEntity> link = repository.findByUrl(url);

        return link.map(linkMapper::fromLinkEntity);
    }
}
