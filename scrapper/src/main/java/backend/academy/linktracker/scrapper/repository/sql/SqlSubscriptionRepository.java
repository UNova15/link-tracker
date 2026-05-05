package backend.academy.linktracker.scrapper.repository.sql;

import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.exception.SubscriptionNotFoundException;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import backend.academy.linktracker.scrapper.repository.sql.dao.SubscriptionDao;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "app.access-type", havingValue = "SQL")
@AllArgsConstructor
public class SqlSubscriptionRepository implements SubscriptionRepository {
    private final SubscriptionDao subscriptionDao;

    @Override
    public List<Long> findChatsIdByLinkId(long linkId) {
        return subscriptionDao.findChatsIdByLinkId(linkId);
    }

    @Override
    public List<Long> findLinksIdByChatId(long chatId) {
        return subscriptionDao.findLinksIdByChatId(chatId);
    }

    @Override
    public List<Subscription> findSubscriptionsByChatId(long chatId) {
        return subscriptionDao.findSubscriptionsByChatId(chatId);
    }

    @Override
    @Transactional
    public void saveSubscription(Subscription subscription) {
        subscriptionDao.saveSubscriptionRecord(subscription.getChatId(), subscription.getLinkId());
        subscriptionDao.saveTagsForSubscription(subscription);
    }

    @Override
    public Subscription removeSubscription(long chatId, long linkId) {
        Subscription subscription = subscriptionDao.findUsersSubscription(chatId, linkId)
            .orElseThrow(() -> new SubscriptionNotFoundException(chatId, linkId));
        subscriptionDao.removeSubscription(chatId, linkId);
        return subscription;
    }

    @Override
    public boolean exist(long chatId, long linkId) {
        return subscriptionDao.exists(chatId, linkId);
    }
}
