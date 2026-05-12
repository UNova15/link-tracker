package backend.academy.linktracker.scrapper.integration.database.orm;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.integration.database.AbstractSubscriptionRepositoryTest;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("orm")
public class OrmSubscriptionRepositoryTest extends AbstractSubscriptionRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Override
    protected void removeSubscription_withValidSubscription_removeSubscription() {
        Subscription subscription =
                createAndSaveExampleOfSubscription(1, LinkType.GIT_HUB, "https://github.com", List.of("tag1", "tag2"));

        Subscription removedSubscription =
                subscriptionRepository.removeSubscription(subscription.getChatId(), subscription.getLinkId());

        entityManager.flush();
        entityManager.clear();

        assertThat(subscriptionRepository.exist(subscription.getChatId(), subscription.getLinkId()))
                .isFalse();
        assertThat(removedSubscription).usingRecursiveComparison().isEqualTo(subscription);
    }
}
