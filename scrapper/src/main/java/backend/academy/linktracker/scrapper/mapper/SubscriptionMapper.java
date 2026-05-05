package backend.academy.linktracker.scrapper.mapper;

import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.repository.orm.entity.SubscriptionEntity;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SubscriptionMapper {
    public Subscription fromSubscriptionEntity(SubscriptionEntity entity) {
        return Subscription.createNew(entity.getChatId(), entity.getLinkId(), entity.getTags());
    }

    public List<Subscription> fromListOfSubscriptionEntities(List<SubscriptionEntity> subscriptionEntities) {
        return subscriptionEntities.stream()
            .map(this::fromSubscriptionEntity)
            .toList();
    }

}
