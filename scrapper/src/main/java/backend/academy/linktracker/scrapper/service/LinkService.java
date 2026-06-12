package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.NotificationRepository;
import backend.academy.linktracker.scrapper.util.LinkParser;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final NotificationRepository notificationRepository;
    private final LinkParser parser;

    public LinkService(
            LinkRepository linkRepository, NotificationRepository notificationRepository, LinkParser parser) {
        this.linkRepository = linkRepository;
        this.notificationRepository = notificationRepository;
        this.parser = parser;
    }

    public Link registerLink(String url) {
        LinkType type = parser.parseLinkType(url);
        Link link = Link.createNew(type, url);
        return linkRepository.save(link);
    }

    @Transactional
    public Link findOrCreateLink(String url) {
        return linkRepository.findByUrl(url).orElseGet(() -> registerLink(url));
    }

    public List<Link> findLinksByIds(List<Long> linkIds) {
        return linkRepository.findAllByIdIn(linkIds);
    }

    public Optional<Link> findByUrl(String url) {
        return linkRepository.findByUrl(url);
    }

    public void deleteLink(String url) {
        linkRepository.removeByUrl(url);
    }

    @Transactional
    public void saveUpdatedLinksAndOutboxRecord(List<Link> checkedLinks, List<Notification> notifications) {
        linkRepository.updateAndMarkAsIdle(checkedLinks);
        if (!notifications.isEmpty()) {
            notificationRepository.save(notifications);
        }
    }

    // реализация двух-шаговой блокировки чтобы не делать сетевые вызовы в рамках транзакции и предотвратить состояние
    // гонки при нескольких потоках/интсансах приложения
    @Transactional
    public List<Link> findLinksFilteredByLastCheck(long lastCheckId, long batchSize, Duration ageOfLinks) {
        // время после которого ссылка считается устаревшей. lastUpdate < delay ( delay = now - ageLink)
        Instant delayTime = Instant.now().minus(ageOfLinks);

        List<Link> links = linkRepository.findLinksFilteredByLastCheck(lastCheckId, batchSize, delayTime);

        List<Long> ids = links.stream().map(Link::getId).toList();
        linkRepository.markAsProcessing(ids);
        return links;
    }
}
