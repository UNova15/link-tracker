package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import backend.academy.linktracker.scrapper.util.LinkParser;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LinkService {
    private final SubscriptionRepository subscriptionRepository;
    private final LinkRepository linkRepository;
    private final LinkParser parser;

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
        if (subscriptionRepository.findChatsIdByLinkId(linkId).isEmpty()) {
            deleteLink(link);
        }
    }
}
