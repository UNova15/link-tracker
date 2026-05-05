package backend.academy.linktracker.scrapper.repository.orm.jparepository;

import backend.academy.linktracker.scrapper.repository.orm.entity.SubscriptionEntity;
import backend.academy.linktracker.scrapper.repository.orm.entity.SubscriptionId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, SubscriptionId> {
    List<Long> findAllChatIdByLinkId(long linkId);

    List<SubscriptionEntity> findAllByChatId(long chatId);

    List<Long> findLinkIdsByChatId(long chatId);
}
