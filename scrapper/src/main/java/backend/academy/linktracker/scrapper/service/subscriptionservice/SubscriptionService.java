package backend.academy.linktracker.scrapper.service.subscriptionservice;

import backend.academy.linktracker.scrapper.dto.linkdto.AddLinkRequest;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.ListLinksResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.RemoveLinkRequest;

public interface SubscriptionService {
    ListLinksResponse findSubscriptionsWithLinks(long chatId);

    LinkResponse createSubscription(long chatId, AddLinkRequest request);

    LinkResponse removeSubscription(long chatId, RemoveLinkRequest request);
}
