package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.NotificationRepository;
import backend.academy.linktracker.scrapper.util.LinkParser;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final NotificationRepository notificationRepository;
    private final SubscriptionService subscriptionService;
    private final LinkParser parser;

    public LinkService(
            LinkRepository linkRepository,
            NotificationRepository notificationRepository,
            @Lazy SubscriptionService subscriptionService,
            LinkParser parser) {
        this.linkRepository = linkRepository;
        this.notificationRepository = notificationRepository;
        this.subscriptionService = subscriptionService;
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
    public void removeUntraceableLinks(long linkId, String link) {
        if (!subscriptionService.isExistsSubscriptionsToLink(linkId)) {
            deleteLink(link);
        }
    }

    @Transactional
    public void saveUpdatedLinksAndOutboxRecord(List<Link> checkedLinks, List<Notification> outboxEvents) {
        // todo для orm реализации будет n+1 запрос из за merge jpa пофиксить не смог
        linkRepository.updateLastCheckAndLastUpdate(checkedLinks);
        if (!outboxEvents.isEmpty()) {
            notificationRepository.save(outboxEvents);
        }
    }

    public List<Link> findLinksFilteredByLastCheck(long lastCheckId, long batchSize, long ageOfLinks) {
        Instant delayTime = Instant.now().minusMillis(ageOfLinks);
        return linkRepository.findLinksFilteredByLastCheck(lastCheckId, batchSize, delayTime);
    }
}
