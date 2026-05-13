package backend.academy.linktracker.scrapper.integration.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import backend.academy.linktracker.scrapper.domain.Chat;
import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.integration.TestcontainersConfiguration;
import backend.academy.linktracker.scrapper.linktracker.LinkTracker;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest("spring.main.lazy-initialization=true")
@Transactional
@Import(TestcontainersConfiguration.class)
public abstract class AbstractSubscriptionRepositoryTest {

    @Autowired
    protected SubscriptionRepository subscriptionRepository;

    @Autowired
    protected ChatRepository chatRepository;

    @Autowired
    protected LinkRepository linkRepository;

    @MockitoBean
    protected LinkTracker linkTracker;

    protected Subscription createAndSaveExampleOfSubscription(
            long chatId, LinkType type, String url, List<String> tags) {
        createAndSaveChat(chatId);
        long linkId = createAndSaveLink(type, url);

        return createAndSaveSubscription(chatId, linkId, tags);
    }

    protected long createAndSaveLink(LinkType type, String url) {
        Link link = Link.createNew(type, url);
        Link savedLink = linkRepository.save(link);
        return savedLink.getId();
    }

    protected void createAndSaveChat(long chatId) {
        Chat chat = Chat.createNew(chatId);
        chatRepository.save(chat);
    }

    protected Subscription createAndSaveSubscription(long chatId, long linkId, List<String> tags) {
        Subscription subscription = Subscription.createNew(chatId, linkId, tags);
        subscriptionRepository.saveSubscription(subscription);
        return subscription;
    }

    // TODO дописать проверку вставки тегов
    @Test
    protected void saveSubscription_withValidSubscription_saveSubscription() {
        Subscription subscription =
                createAndSaveExampleOfSubscription(1, LinkType.GIT_HUB, "https://github.com", List.of("tag1", "tag2"));

        assertThat(subscriptionRepository.exists(subscription.getChatId(), subscription.getLinkId()))
                .isTrue();
    }

    @Test
    protected void saveSubscription_withDuplicateSubscription_throwException() {
        Subscription subscription =
                createAndSaveExampleOfSubscription(1, LinkType.GIT_HUB, "https://github.com", List.of("tag1", "tag2"));

        assertThatThrownBy(() -> subscriptionRepository.saveSubscription(subscription))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    protected void removeSubscription_withValidSubscription_removeSubscription() {
        Subscription subscription =
                createAndSaveExampleOfSubscription(1, LinkType.GIT_HUB, "https://github.com", List.of("tag1", "tag2"));

        Subscription removedSubscription =
                subscriptionRepository.removeSubscription(subscription.getChatId(), subscription.getLinkId());

        assertThat(subscriptionRepository.exists(subscription.getChatId(), subscription.getLinkId()))
                .isFalse();
        assertThat(removedSubscription).usingRecursiveComparison().isEqualTo(subscription);
    }

    @Test
    protected void findSubscriptionsByChatId_withExistingChats_returnSubscriptions() {
        long chatId = 1;
        createAndSaveChat(chatId);

        int size = 10;
        List<Subscription> subscriptions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            long linkId = createAndSaveLink(LinkType.GIT_HUB, "https://github.com" + i);
            Subscription subscription = createAndSaveSubscription(chatId, linkId, List.of("tag1", "tag2"));
            subscriptions.add(subscription);
        }

        List<Subscription> actualSubscriptions = subscriptionRepository.findSubscriptionsByChatId(chatId);

        assertThat(actualSubscriptions)
                .hasSize(size)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(subscriptions);
    }

    @Test
    protected void findSubscriptionsByChatId_withNonExistingChat_returnEmptyList() {
        long nonExistingChatId = 0;
        int size = 10;

        for (int chatId = 1; chatId <= size; chatId++) {
            createAndSaveExampleOfSubscription(
                    chatId, LinkType.GIT_HUB, "https://github.com" + chatId, List.of("tag1", "tag2"));
        }

        List<Subscription> actualSubscriptions = subscriptionRepository.findSubscriptionsByChatId(nonExistingChatId);

        assertThat(actualSubscriptions).isEmpty();
    }

    @Test
    protected void findChatsIdByLinkId_withSubscribedLink_returnListOfChatId() {
        long nonExpectedLinkId = createAndSaveLink(LinkType.GIT_HUB, "https://stackoverflow.com");
        long expectedLinkId = createAndSaveLink(LinkType.STACK_OVERFLOW, "https://github.com");
        int size = 10;

        List<String> tags = List.of("tag1", "tag2");
        List<Long> expectedChatIds = new ArrayList<>();
        for (long chatId = 0; chatId < size; chatId++) {
            createAndSaveChat(chatId);
            createAndSaveSubscription(chatId, expectedLinkId, tags);
            expectedChatIds.add(chatId);
        }
        createAndSaveChat(11);
        createAndSaveChat(12);
        createAndSaveSubscription(11, nonExpectedLinkId, tags);
        createAndSaveSubscription(12, nonExpectedLinkId, tags);

        List<Long> actualChatIds = subscriptionRepository.findChatsIdByLinkId(expectedLinkId);

        assertThat(actualChatIds).hasSize(size).containsExactlyInAnyOrderElementsOf(expectedChatIds);
    }

    @Test
    protected void findChatsIdByLinkId_withNonSubscribedLink_returnEmptyList() {
        long expectedLinkId = createAndSaveLink(LinkType.GIT_HUB, "https://stackoverflow.com");
        long nonExpectedLinkId = createAndSaveLink(LinkType.STACK_OVERFLOW, "https://github.com");
        int size = 10;

        List<String> tags = List.of("tag1", "tag2");
        List<Long> chatIds = new ArrayList<>();
        for (long chatId = 0; chatId < size; chatId++) {
            createAndSaveChat(chatId);
            createAndSaveSubscription(chatId, nonExpectedLinkId, tags);
            chatIds.add(chatId);
        }

        List<Long> actualChatIds = subscriptionRepository.findChatsIdByLinkId(expectedLinkId);

        assertThat(actualChatIds).isEmpty();
    }
}
