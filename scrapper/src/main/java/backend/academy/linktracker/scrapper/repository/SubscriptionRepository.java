package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.model.Subscription;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Repository
public class SubscriptionRepository {
    private final Set<Subscription> subscriptions = new HashSet<>();

    public List<Long> findChatsIdByLinkId(long linkId) {
        return subscriptions.stream()
            .filter(subscription -> subscription.linkId() == linkId)
            .map(Subscription::chatId)
            .toList();
    }

    public List<Long> findLinksIdByChatId(long chatId) {
        return subscriptions.stream()
            .filter(subscription -> subscription.chatId() == chatId)
            .map(Subscription::linkId)
            .toList();
    }

    public void saveSubscription(long chatId, long linkId) {
        subscriptions.add(new Subscription(chatId, linkId));
    }

    public void removeSubscription(long chatId, long linkId) {
        subscriptions.remove(new Subscription(chatId, linkId));
    }

    public boolean exist(long chatId, long linkId) {
        long count = subscriptions.stream()
            .filter(subscription -> subscription.linkId() == linkId && subscription.chatId() == chatId)
            .count();

        return count != 0;
    }
}
