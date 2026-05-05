package backend.academy.linktracker.scrapper.repository.orm.entity;

import backend.academy.linktracker.scrapper.domain.Subscription;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import java.util.List;

@Entity
@Table(name = "subscriptions")
@IdClass(SubscriptionId.class)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionEntity implements Persistable<SubscriptionId> {
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Id
    @Column(name = "link_id", nullable = false)
    private Long linkId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "tags_subscriptions",
        joinColumns = {
            @JoinColumn(name = "chat_id", referencedColumnName = "chat_id"),
            @JoinColumn(name = "link_id", referencedColumnName = "link_id")
        }
    )
    @Column(name = "tag", nullable = false)
    private List<String> tags;

    @Transient
    private boolean isNew;

    public static SubscriptionEntity createFromSubscription(Subscription subscription) {
        return new SubscriptionEntity(subscription.getChatId(), subscription.getLinkId(), subscription.getTags(), true);
    }

    @Override
    public SubscriptionId getId() {
        return new SubscriptionId(chatId, linkId);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    public void markNotNew() {
        this.isNew = false;
    }
}
