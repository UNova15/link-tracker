package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.dto.linkdto.AddLinkRequest;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.ListLinksResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.RemoveLinkRequest;
import backend.academy.linktracker.scrapper.exception.ChatNotFoundException;
import backend.academy.linktracker.scrapper.exception.LinkAlreadyRegistratedException;
import backend.academy.linktracker.scrapper.exception.LinkNotFoundException;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.parser.LinkParser;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinkService {
    private final LinkParser parser;
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ListLinksResponse findLinksByChatId(long chatId) {
        if (!chatRepository.exists(chatId)) {
            throw new ChatNotFoundException(chatId);
        }
        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsByChatId(chatId);
        List<Long> linksId = subscriptions.stream().map(Subscription::linkId).toList();

        List<Link> links = linkRepository.findLinksByLinksId(linksId);
        return new ListLinksResponse(links, subscriptions);
    }

    public LinkResponse saveLink(long chatId, AddLinkRequest request) {
        if (!chatRepository.exists(chatId)) {
            throw new ChatNotFoundException(chatId);
        }

        Link link = linkRepository.findLinkByUrl(request.url()).orElseGet(() -> {
            LinkType type = parser.parseLinkType(request.url());
            return linkRepository.saveLink(type, request.url(), Instant.now());
        });

        if (subscriptionRepository.exist(chatId, link.id())) {
            throw new LinkAlreadyRegistratedException(request.url(), chatId);
        }

        subscriptionRepository.saveSubscription(chatId, link.id(), request.tags());
        return new LinkResponse(link.id(), link.url(), request.tags());
    }

    public LinkResponse removeLink(long chatId, RemoveLinkRequest request) {
        if (!chatRepository.exists(chatId)) {
            throw new ChatNotFoundException(chatId);
        }

        Link link = linkRepository
                .findLinkByUrl(request.link())
                .orElseThrow(() -> new LinkNotFoundException(request.link()));

        if (!subscriptionRepository.exist(chatId, link.id())) {
            throw new LinkNotFoundException(request.link(), chatId);
        }
        Subscription subscription = subscriptionRepository.removeSubscription(chatId, link.id());

        // проверка существования пользователей отслеживающих ссылку
        if (subscriptionRepository.findChatsIdByLinkId(link.id()).isEmpty()) {
            linkRepository.removeLink(link.url());
        }
        return new LinkResponse(chatId, request.link(), subscription.tags());
    }
}
