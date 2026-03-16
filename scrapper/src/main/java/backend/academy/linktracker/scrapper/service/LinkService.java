package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.exception.ChatNotFoundException;
import backend.academy.linktracker.scrapper.exception.LinkAlreadyExistException;
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

        if (linkRepository.exists(request.url())) {
            throw new LinkAlreadyExistException(request.url(), chatId);
        }
        LinkType type = parser.parseLinkType(request.url());

        Link link = linkRepository.saveLink(type, request.url(), request.tags(), Instant.now());
        subscriptionRepository.saveSubscription(chatId, link.getId());

        return new LinkResponse(link.getId(), link.getUrl(), link.getTags());
    }

    public LinkResponse removeLink(long chatId, RemoveLinkRequest request) {
        if (!chatRepository.exists(chatId)) {
            throw new ChatNotFoundException(chatId);
        }

        List<Long> linksId = subscriptionRepository.findLinksIdByChatId(chatId);
        Link link = linkRepository.findLinkByUrl(request.link()).orElseThrow(() -> new LinkNotFoundException(request.link()));

        if (!linksId.contains(link.getId())) {
            throw new LinkNotFoundException(request.link(), chatId);
        }

        subscriptionRepository.removeSubscription(chatId, link.getId());

        //проверка существования пользователей отслеживающих ссылку
        boolean isActive = false;
        for (Subscription subscription : subscriptionRepository.getSubscriptions()) {
            if (subscription.linkId() == link.getId()) {
                isActive = true;
                break;
            }
        }

        if (!isActive) {
            linkRepository.removeLink(link.getUrl());
        }
        return new LinkResponse(chatId, link.getUrl(), link.getTags());
    }
}
