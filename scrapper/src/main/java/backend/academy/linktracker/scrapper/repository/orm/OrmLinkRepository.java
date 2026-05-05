package backend.academy.linktracker.scrapper.repository.orm;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.mapper.LinkMapper;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.orm.entity.LinkEntity;
import backend.academy.linktracker.scrapper.repository.orm.jparepository.LinkJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.access-type", havingValue = "ORM")
@AllArgsConstructor
public class OrmLinkRepository implements LinkRepository {
    private final LinkJpaRepository repository;
    private final LinkMapper linkMapper;

    @Override
    public List<Link> getAll() {
        List<LinkEntity> links = repository.findAll();
        return linkMapper.fromListLinkEntity(links);
    }

    @Override
    public Link save(Link link) {
        LinkEntity linkEntity = LinkEntity.fromLink(link);
        LinkEntity savedLink = repository.save(linkEntity);
        return linkMapper.fromLinkEntity(savedLink);
    }

    @Override
    public List<Link> findAllByIdIn(List<Long> linksId) {
        List<LinkEntity> linkEntities = repository.findAllById(linksId);
        return linkMapper.fromListLinkEntity(linkEntities);
    }

    @Override
    public void update(Link link) {
        LinkEntity linkEntity = LinkEntity.fromLink(link);
        repository.save(linkEntity);
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
