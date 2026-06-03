package backend.academy.linktracker.scrapper.service.subscriptionservice;

import backend.academy.linktracker.scrapper.configuration.ClusterClientSideCache;
import backend.academy.linktracker.scrapper.dto.linkdto.AddLinkRequest;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.ListLinksResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.RemoveLinkRequest;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@CacheConfig(cacheNames = {"getLinks"})
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceProxy implements SubscriptionService {
    private static final String CACHE_NAME = "getLinks::%d";

    @Value("${app.cache.valkey.ttl}")
    @DurationUnit(ChronoUnit.HOURS)
    private Duration ttl;

    private final SubscriptionServiceImpl service;
    private final ClusterClientSideCache clusterCache;

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse findSubscriptionsWithLinks(long chatId) {
        String key = CACHE_NAME.formatted(chatId);
        ListLinksResponse value = clusterCache.get(key);

        if (value == null) {
            ListLinksResponse actualValue = service.findSubscriptionsWithLinks(chatId);

            clusterCache.put(key, actualValue, ttl);
            return actualValue;
        }
        return value;
    }

    @Override
    @Transactional
    @CacheEvict(key = "#chatId")
    public LinkResponse createSubscription(long chatId, AddLinkRequest request) {
        return service.createSubscription(chatId, request);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#chatId")
    public LinkResponse removeSubscription(long chatId, RemoveLinkRequest request) {
        return service.removeSubscription(chatId, request);
    }

    @Override
    public boolean isExistsSubscriptionsToLink(long linkId) {
        return service.isExistsSubscriptionsToLink(linkId);
    }
}
