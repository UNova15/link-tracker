package backend.academy.linktracker.scrapper.integration.external;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import backend.academy.linktracker.scrapper.domain.Chat;
import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.integration.TestContainersConfiguration;
import backend.academy.linktracker.scrapper.linktracker.LinkProcessor;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

// Тестирование логики обработки ссылок и формирования пользовательского сообщения
@SpringBootTest({"spring.main.lazy-initialization=true", "app.sender=http", "app.db-provider=sql"})
@Import(TestContainersConfiguration.class)
@Transactional
@ActiveProfiles("test")
public class ProcessLinksTest {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private LinkProcessor linkProcessor;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @MockitoBean
    private ExecutorService executorService;

    @RegisterExtension
    static WireMockExtension github = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.github.base-url", github::baseUrl);
        registry.add("app.stackoverflow.base-url", github::baseUrl);
    }

    @BeforeEach
    void settings() {
        doAnswer(invocation -> {
                    Runnable runnable = invocation.getArgument(0);
                    runnable.run();
                    return null;
                })
                .when(executorService)
                .execute(any(Runnable.class));
    }

    @Test
    void processLinks_gitHubReturnNewIssue_sendNotification() {
        Chat chat = Chat.createNew(1);
        chatRepository.save(chat);

        Link link = linkRepository.save(Link.createNew(LinkType.GIT_HUB, "https://github.com/UNova15/my_project"));

        Subscription subscription = Subscription.createNew(chat.getChatId(), link.getId(), List.of());
        subscriptionRepository.saveSubscription(subscription);

        github.stubFor(WireMock.get(WireMock.urlPathMatching("/repos/UNova15/my_project/issues"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            [
                              {
                                "title": "Test Issue",
                                "user": { "login": "test_user" },
                                "updated_at": "%s",
                                "body": "This is a test description"
                              }
                            ]
                            """.formatted(Instant.now().plusSeconds(10)))));

        List<Notification> notifications = linkProcessor.runProcessLinks(List.of(link));
        assertThat(notifications).hasSize(1);

        Notification notification = notifications.getFirst();

        assertThat(notification.getLinkId()).isEqualTo(link.getId());
        assertThat(notification.getUrl()).isEqualTo(link.getUrl());
        assertThat(notification.getTgChatIds()).containsExactly(chat.getChatId());

        assertThat(notification.getDescription())
                .contains("Обновление в GitHub")
                .contains("Test Issue")
                .contains("test_user")
                .contains("This is a test description");
    }

    @Test
    void processLinks_stackOverflowReturnNewAnswer_sendNotification() {
        Chat chat = Chat.createNew(1);
        chatRepository.save(chat);

        Link link = linkRepository.save(
                Link.createNew(LinkType.STACK_OVERFLOW, "https://stackoverflow.com/questions/44420613/is"));

        Subscription subscription = Subscription.createNew(chat.getChatId(), link.getId(), List.of());
        subscriptionRepository.saveSubscription(subscription);

        github.stubFor(WireMock.get(WireMock.urlPathMatching("/questions/44420613"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                                "items": [
                                    {
                                        "title": "Test title",
                                        "answers": [],
                                        "comments": [
                                            {
                                                "owner": {
                                                    "display_name": "Kirill"
                                                },
                                                "creation_date": %d,
                                                "body": "It test comment"
                                            }
                                        ]
                                    }
                                ],
                                "has_more": false,
                                "quota_max": 10,
                                "quota_remaining": 0
                            }
                            """.formatted(Instant.now().plusSeconds(10).getEpochSecond()))));

        List<Notification> notifications = linkProcessor.runProcessLinks(List.of(link));
        assertThat(notifications).hasSize(1);

        Notification notification = notifications.getFirst();

        assertThat(notification.getLinkId()).isEqualTo(link.getId());
        assertThat(notification.getUrl()).isEqualTo(link.getUrl());
        assertThat(notification.getTgChatIds()).containsExactly(chat.getChatId());

        assertThat(notification.getDescription())
                .contains("Обновление в StackOverFlow")
                .contains("Test title")
                .contains("Kirill")
                .contains("It test comment");
    }

    @Test
    void processLinks_withPreviewLongerThanCharactersLimit_willCropPreview() {
        Chat chat = Chat.createNew(1);
        chatRepository.save(chat);

        Link link = linkRepository.save(Link.createNew(LinkType.GIT_HUB, "https://github.com/UNova15/my_project"));

        Subscription subscription = Subscription.createNew(chat.getChatId(), link.getId(), List.of());
        subscriptionRepository.saveSubscription(subscription);

        github.stubFor(WireMock.get(WireMock.urlPathMatching("/repos/UNova15/my_project/issues"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            [
                              {
                                "title": "Test Issue",
                                "user": { "login": "test_user" },
                                "updated_at": "%s",
                                "body": "This is a test description and This is a test description and This is a test description and This is a test description and This is a test description and This is a test description and This is a test description and This is a test description"
                              }
                            ]
                            """.formatted(Instant.now().plusSeconds(10)))));

        List<Notification> notifications = linkProcessor.runProcessLinks(List.of(link));
        assertThat(notifications).hasSize(1);

        Notification notification = notifications.getFirst();
        assertThat(notification.getDescription())
                .contains("This is a test description and This is a test description and This is a test description and"
                        + " This is a test description and This is a test description and This is a test"
                        + " description and This is a tes...");
    }

    @Test
    void processLinks_withUnavailableGitHubApi_sendErrorMessage() {
        Chat chat = Chat.createNew(1);
        chatRepository.save(chat);

        Link link = linkRepository.save(Link.createNew(LinkType.GIT_HUB, "https://github.com/UNova15/my_project"));

        Subscription subscription = Subscription.createNew(chat.getChatId(), link.getId(), List.of());
        subscriptionRepository.saveSubscription(subscription);

        github.stubFor(WireMock.get(WireMock.urlPathMatching("/repos/UNova15/my_project/issues"))
                .willReturn(aResponse().withStatus(500)));

        List<Notification> notifications = linkProcessor.runProcessLinks(List.of(link));
        assertThat(notifications).hasSize(1);

        Notification notification = notifications.getFirst();
        assertThat(notification.getLinkId()).isEqualTo(link.getId());
        assertThat(notification.getUrl()).isEqualTo(link.getUrl());
        assertThat(notification.getTgChatIds()).containsExactly(chat.getChatId());

        assertThat(notification.getDescription())
                .contains("Ошибка проверки ссылки: https://github.com/UNova15/my_project");
    }

    @Test
    void processLinks_withErrorInBatchProcessing_processValidLinks() {
        Chat chat = Chat.createNew(1);
        chatRepository.save(chat);

        Link link1 = linkRepository.save(Link.createNew(LinkType.GIT_HUB, "https://github.com/UNova15/my_project1"));
        Link link2 = linkRepository.save(Link.createNew(LinkType.GIT_HUB, "https://github.com/UNova15/my_project2"));

        Subscription subscription1 = Subscription.createNew(chat.getChatId(), link1.getId(), List.of());
        Subscription subscription2 = Subscription.createNew(chat.getChatId(), link2.getId(), List.of());

        subscriptionRepository.saveSubscription(subscription1);
        subscriptionRepository.saveSubscription(subscription2);

        github.stubFor(WireMock.get(WireMock.urlPathMatching("/repos/UNova15/my_project1/issues"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            [
                              {
                                "title": "Test Issue",
                                "user": { "login": "test_user" },
                                "updated_at": "%s",
                                "body": "This is a test description"
                              }
                            ]
                            """.formatted(Instant.now().plusSeconds(10)))));

        github.stubFor(WireMock.get(WireMock.urlPathMatching("/repos/UNova15/my_project2/issues"))
                .willReturn(aResponse().withStatus(500)));

        List<Notification> notifications = linkProcessor.runProcessLinks(List.of(link1, link2));
        assertThat(notifications).hasSize(2);

        Notification firstUpdate = notifications.getFirst();
        assertThat(firstUpdate.getLinkId()).isEqualTo(link1.getId());
        assertThat(firstUpdate.getUrl()).isEqualTo(link1.getUrl());
        assertThat(firstUpdate.getTgChatIds()).containsExactly(chat.getChatId());

        assertThat(firstUpdate.getDescription())
                .contains("Обновление в GitHub")
                .contains("Test Issue")
                .contains("test_user")
                .contains("This is a test description");

        Notification secondUpdate = notifications.get(1);
        assertThat(secondUpdate.getLinkId()).isEqualTo(link2.getId());
        assertThat(secondUpdate.getUrl()).isEqualTo(link2.getUrl());
        assertThat(secondUpdate.getTgChatIds()).containsExactly(chat.getChatId());

        assertThat(secondUpdate.getDescription())
                .contains("Ошибка проверки ссылки: https://github.com/UNova15/my_project2");
    }
}
