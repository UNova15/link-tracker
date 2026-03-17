package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.exception.ChatNotFoundException;
import backend.academy.linktracker.scrapper.exception.LinkAlreadyRegistratedException;
import backend.academy.linktracker.scrapper.exception.LinkNotFoundException;
import backend.academy.linktracker.scrapper.model.Subscription;
import backend.academy.linktracker.scrapper.model.linkdto.*;
import backend.academy.linktracker.scrapper.model.linkdto.Link;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.parser.LinkParser;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class LinkService {

    private final LinkParser parser;
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository, ChatRepository chatRepository,
                       SubscriptionRepository subscriptionRepository, LinkParser parser) {
        this.linkRepository = linkRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.chatRepository = chatRepository;
        this.parser = parser;
    }

    public ListLinksResponse findLinksByChatId(long chatId) {
        if (!chatRepository.exists(chatId)) {
            throw new ChatNotFoundException(chatId);
        }
        List<Long> linksId = subscriptionRepository.findLinksIdByChatId(chatId);


        List<Link> links = linkRepository.findLinksByLinksId(linksId);
        return new ListLinksResponse(chatId, links);
    }

    public LinkResponse saveLink(long chatId, AddLinkRequest request) {
        if (!chatRepository.exists(chatId)) {
            throw new ChatNotFoundException(chatId);
        }

        Link link = linkRepository.findLinkByUrl(request.url())
            .orElseGet(() -> {
                LinkType type = parser.parseLinkType(request.url());
                return linkRepository.saveLink(type, request.url(), request.tags(), Instant.now());
            });

        if (subscriptionRepository.exist(chatId, link.id())) {
            throw new LinkAlreadyRegistratedException(request.url(), chatId);
        }

        subscriptionRepository.saveSubscription(chatId, link.id());
        return new LinkResponse(link.id(), link.url(), link.tags());
    }

    public LinkResponse removeLink(long chatId, RemoveLinkRequest request) {
        if (!chatRepository.exists(chatId)) {
            throw new ChatNotFoundException(chatId);
        }

        List<Long> linksId = subscriptionRepository.findLinksIdByChatId(chatId);
        Link link = linkRepository.findLinkByUrl(request.link()).orElseThrow(() -> new LinkNotFoundException(request.link()));

        if (!linksId.contains(link.id())) {
            throw new LinkNotFoundException(request.link(), chatId);
        }
        subscriptionRepository.removeSubscription(chatId, link.id());

        //проверка существования пользователей отслеживающих ссылку
        boolean isActive = false;
        for (Subscription subscription : subscriptionRepository.getSubscriptions()) {
            if (subscription.linkId() == link.id()) {
                isActive = true;
                break;
            }
        }

        if (!isActive) {
            linkRepository.removeLink(link.url());
        }
        return new LinkResponse(chatId, link.url(), link.tags());
    }
}
