package backend.academy.linktracker.scrapper.service.subscriptionservice;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.dto.linkdto.AddLinkRequest;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.ListLinksResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.RemoveLinkRequest;
import backend.academy.linktracker.scrapper.exception.ChatNotFoundException;
import backend.academy.linktracker.scrapper.exception.LinkAlreadyRegistratedException;
import backend.academy.linktracker.scrapper.exception.LinkNotFoundException;
import backend.academy.linktracker.scrapper.mapper.LinkMapper;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import backend.academy.linktracker.scrapper.service.LinkService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final LinkService linkService;

    private final ChatRepository chatRepository;
    private final SubscriptionRepository subscriptionRepository;

    private final LinkMapper linkMapper;

    @Override
    public ListLinksResponse findSubscriptionsWithLinks(long chatId) {
        if (!chatRepository.existById(chatId)) {
            throw new ChatNotFoundException(chatId);
        }
        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsByChatId(chatId);
        List<Long> linksId = subscriptions.stream().map(Subscription::getLinkId).toList();

        List<Link> links = linkService.findLinksByIds(linksId);
        return linkMapper.toListLinkResponse(subscriptions, links);
    }

    @Override
    public LinkResponse createSubscription(long chatId, AddLinkRequest request) {
        if (!chatRepository.existById(chatId)) {
            throw new ChatNotFoundException(chatId);
        }

        Link link = linkService.findOrCreateLink(request.url());

        if (subscriptionRepository.exists(chatId, link.getId())) {
            throw new LinkAlreadyRegistratedException(request.url(), chatId);
        }

        Subscription subscription = Subscription.createNew(chatId, link.getId(), request.tags());

        subscriptionRepository.saveSubscription(subscription);
        return linkMapper.toLinkResponse(link, subscription.getTags());
    }

    @Override
    public LinkResponse removeSubscription(long chatId, RemoveLinkRequest request) {
        if (!chatRepository.existById(chatId)) {
            throw new ChatNotFoundException(chatId);
        }

        Link link = linkService.findByUrl(request.link()).orElseThrow(() -> new LinkNotFoundException(request.link()));

        Subscription subscription = subscriptionRepository.removeSubscription(chatId, link.getId());
        removeUntraceableLinks(link.getId(), link.getUrl());

        return linkMapper.toLinkResponse(link, subscription.getTags());
    }

    private void removeUntraceableLinks(long linkId, String link) {
        if (subscriptionRepository.findChatsIdByLinkId(linkId).isEmpty()) {
            linkService.deleteLink(link);
        }
    }
}
