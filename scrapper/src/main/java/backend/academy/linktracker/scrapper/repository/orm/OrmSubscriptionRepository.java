package backend.academy.linktracker.scrapper.repository.orm;

import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.exception.SubscriptionNotFoundException;
import backend.academy.linktracker.scrapper.mapper.SubscriptionMapper;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import backend.academy.linktracker.scrapper.repository.orm.entity.SubscriptionEntity;
import backend.academy.linktracker.scrapper.repository.orm.entity.SubscriptionId;
import backend.academy.linktracker.scrapper.repository.orm.jparepository.SubscriptionJpaRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("orm")
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
    public void saveSubscription(Subscription subscription) {
        SubscriptionEntity entity = SubscriptionEntity.createFromSubscription(subscription);
        repository.save(entity);
    }

    @Override
    public Subscription removeSubscription(long chatId, long linkId) {
        SubscriptionId subscriptionId = new SubscriptionId(chatId, linkId);

        SubscriptionEntity entity = repository
                .findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(chatId, linkId));

        Subscription subscription = mapper.fromSubscriptionEntity(entity);
        repository.deleteById(subscriptionId);

        return subscription;
    }

    @Override
    public boolean exist(long chatId, long linkId) {
        SubscriptionId subscriptionId = new SubscriptionId(chatId, linkId);
        return repository.existsById(subscriptionId);
    }
}
