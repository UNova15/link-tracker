package backend.academy.linktracker.scrapper.integration.external;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import backend.academy.linktracker.scrapper.domain.Chat;
import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.integration.TestcontainersConfiguration;
import backend.academy.linktracker.scrapper.linktracker.LinkProcessor;
import backend.academy.linktracker.scrapper.messagesender.MessageSender;
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
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest("spring.main.lazy-initialization=true")
@Import(TestcontainersConfiguration.class)
@Transactional
@ActiveProfiles({"sql", "test"})
public class GitHubAndStackOverflowApiTest {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private LinkProcessor linkProcessor;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @MockitoBean
    private MessageSender messageSender;

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
                        "updated_at": "2026-05-10T12:00:00Z",
                        "body": "This is a test description"
                      }
                    ]
                    """)));

        linkProcessor.runProcessLinks(List.of(link));

        ArgumentCaptor<LinkUpdate> captor = ArgumentCaptor.forClass(LinkUpdate.class);
        verify(messageSender, times(1)).send(captor.capture());

        LinkUpdate actualUpdate = captor.getValue();
        assertThat(actualUpdate.id()).isEqualTo(link.getId());
        assertThat(actualUpdate.url()).isEqualTo(link.getUrl());
        assertThat(actualUpdate.tgChatIds()).containsExactly(chat.getChatId());

        assertThat(actualUpdate.description())
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

        linkProcessor.runProcessLinks(List.of(link));

        ArgumentCaptor<LinkUpdate> captor = ArgumentCaptor.forClass(LinkUpdate.class);
        verify(messageSender, times(1)).send(captor.capture());

        LinkUpdate actualUpdate = captor.getValue();
        assertThat(actualUpdate.id()).isEqualTo(link.getId());
        assertThat(actualUpdate.url()).isEqualTo(link.getUrl());
        assertThat(actualUpdate.tgChatIds()).containsExactly(chat.getChatId());

        assertThat(actualUpdate.description())
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
                        "updated_at": "2026-05-10T12:00:00Z",
                        "body": "This is a test description and This is a test description and This is a test description and This is a test description and This is a test description and This is a test description and This is a test description and This is a test description"
                      }
                    ]
                    """)));

        linkProcessor.runProcessLinks(List.of(link));

        ArgumentCaptor<LinkUpdate> captor = ArgumentCaptor.forClass(LinkUpdate.class);
        verify(messageSender, times(1)).send(captor.capture());

        LinkUpdate actualUpdate = captor.getValue();

        assertThat(actualUpdate.description())
                .contains(
                        "This is a test description and This is a test description and This is a test description and This is a test description and This is a test description and This is a test description and This is a tes...");
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

        linkProcessor.runProcessLinks(List.of(link));

        ArgumentCaptor<LinkUpdate> captor = ArgumentCaptor.forClass(LinkUpdate.class);
        verify(messageSender, times(1)).send(captor.capture());

        LinkUpdate actualUpdate = captor.getValue();
        assertThat(actualUpdate.id()).isEqualTo(link.getId());
        assertThat(actualUpdate.url()).isEqualTo(link.getUrl());
        assertThat(actualUpdate.tgChatIds()).containsExactly(chat.getChatId());

        assertThat(actualUpdate.description())
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
                        "updated_at": "2026-05-10T12:00:00Z",
                        "body": "This is a test description"
                      }
                    ]
                    """)));

        github.stubFor(WireMock.get(WireMock.urlPathMatching("/repos/UNova15/my_project2/issues"))
                .willReturn(aResponse().withStatus(500)));

        linkProcessor.runProcessLinks(List.of(link1, link2));

        ArgumentCaptor<LinkUpdate> captor = ArgumentCaptor.forClass(LinkUpdate.class);
        verify(messageSender, times(2)).send(captor.capture());

        LinkUpdate firstUpdate = captor.getAllValues().getFirst();
        assertThat(firstUpdate.id()).isEqualTo(link1.getId());
        assertThat(firstUpdate.url()).isEqualTo(link1.getUrl());
        assertThat(firstUpdate.tgChatIds()).containsExactly(chat.getChatId());

        assertThat(firstUpdate.description())
                .contains("Обновление в GitHub")
                .contains("Test Issue")
                .contains("test_user")
                .contains("This is a test description");

        LinkUpdate secondUpdate = captor.getAllValues().getLast();
        assertThat(secondUpdate.id()).isEqualTo(link1.getId());
        assertThat(secondUpdate.url()).isEqualTo(link1.getUrl());
        assertThat(secondUpdate.tgChatIds()).containsExactly(chat.getChatId());

        assertThat(secondUpdate.description())
                .contains("Ошибка проверки ссылки: https://github.com/UNova15/my_project2");
    }
}
