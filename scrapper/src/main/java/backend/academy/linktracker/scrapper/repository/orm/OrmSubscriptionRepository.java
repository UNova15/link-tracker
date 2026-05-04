package backend.academy.linktracker.scrapper.repository.orm.entity;

import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.mapper.SubscriptionMapper;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import backend.academy.linktracker.scrapper.repository.orm.jparepository.SubscriptionJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@AllArgsConstructor
public class OrmSubscriptionRepository implements SubscriptionRepository {
    private final SubscriptionJpaRepository repository;
    private final SubscriptionMapper mapper;

    @Override
    public List<Long> findChatsIdByLinkId(long linkId) {
        return repository.findAllChatIdByLinkId(linkId);
    }

    @Override
    public List<Subscription> findSubscriptionsByChatId(long chatId) {
        List<SubscriptionEntity> subscriptionEntities = repository.findAllByChatId(chatId);
        return mapper.fromListOfSubscriptionEntities(subscriptionEntities);
    }

    @Override
    public List<Long> findLinksIdByChatId(long chatId) {
        return repository.findLinkIdsByChatId(chatId);
    }

    @Override
    public void saveSubscription(Subscription subscription) {
        SubscriptionEntity entity = SubscriptionEntity.createFromSubscription(subscription);
        repository.save(entity);
    }

    @Override
    public Subscription removeSubscription(long chatId, long linkId) {
        SubscriptionId subscriptionId = new SubscriptionId(chatId, linkId);

        SubscriptionEntity entity = repository.findById(subscriptionId)
            .orElseThrow(()-> new SubscriptionNotExist());
        Subscription subscription =
        return repository.deleteById(subscriptionId);
    }

    @Override
    public boolean exist(long chatId, long linkId) {
        return false;
    }
}
