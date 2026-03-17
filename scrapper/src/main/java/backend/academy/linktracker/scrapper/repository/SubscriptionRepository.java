package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.exception.LinkNotFoundException;
import backend.academy.linktracker.scrapper.model.Subscription;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Repository
public class SubscriptionRepository {
    private final Set<Subscription> subscriptions = new HashSet<>();

    public List<Long> findChatsIdByLinkId(long linkId) {
        return subscriptions.stream()
            .filter(subscription -> subscription.linkId() == linkId)
            .map(Subscription::chatId)
            .toList();
    }

    public List<Subscription> findSubscriptionsByChatId(long chatId) {
        return subscriptions.stream()
            .filter(subscription -> subscription.chatId() == chatId)
            .toList();
    }

    public List<Long> findLinksIdByChatId(long chatId){
        return findSubscriptionsByChatId(chatId).stream()
            .map(Subscription::linkId)
            .toList();
    }

    public void saveSubscription(long chatId, long linkId, List<String> tags) {
        subscriptions.add(new Subscription(chatId, linkId, tags));
    }

    public Subscription removeSubscription(long chatId, long linkId) {
        Subscription subscription = subscriptions.stream()
            .filter(sub -> sub.linkId() == linkId && sub.chatId() == chatId)
            .findFirst().orElseThrow(() -> new LinkNotFoundException(chatId, linkId));
        subscriptions.remove(subscription);
        return subscription;
    }

    public boolean exist(long chatId, long linkId) {
        return subscriptions.stream()
            .anyMatch(subscription -> subscription.linkId() == linkId && subscription.chatId() == chatId);
    }
}
