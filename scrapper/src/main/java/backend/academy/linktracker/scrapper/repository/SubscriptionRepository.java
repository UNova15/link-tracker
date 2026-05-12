package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.domain.Subscription;
import java.util.List;

public interface SubscriptionRepository {

    List<Long> findChatsIdByLinkId(long linkId);

    List<Subscription> findSubscriptionsByChatId(long chatId);

    void saveSubscription(Subscription subscription);

    Subscription removeSubscription(long chatId, long linkId);

    boolean exist(long chatId, long linkId);
}
