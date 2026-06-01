package backend.academy.linktracker.scrapper.integration.valkey;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import backend.academy.linktracker.scrapper.dto.linkdto.AddLinkRequest;
import backend.academy.linktracker.scrapper.dto.linkdto.ListLinksResponse;
import backend.academy.linktracker.scrapper.integration.TestContainersConfiguration;
import backend.academy.linktracker.scrapper.messagesender.KafkaClient;
import backend.academy.linktracker.scrapper.service.subscriptionservice.SubscriptionService;
import backend.academy.linktracker.scrapper.service.subscriptionservice.SubscriptionServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Import({TestContainersConfiguration.class, ValkeyClusterTestConfig.class})
class SubscriptionServiceCacheTest {

    @Container
    static GenericContainer<?> valkey = new GenericContainer<>("valkey/valkey:8.0")
            .withExposedPorts(6379)
            .withCommand("valkey-server", "--cluster-enabled", "yes", "--cluster-node-timeout", "5000");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) throws Exception {
        valkey.execInContainer("sh", "-c", "valkey-cli cluster addslots $(seq 0 16383)");

        String valkeyAddress = valkey.getHost() + ":" + valkey.getFirstMappedPort();
        registry.add("spring.data.redis.cluster.nodes", () -> valkeyAddress);
        registry.add("app.cache-ttl", () -> "1");
        registry.add("app.max-size-local-cache", () -> "100");
    }

    @Autowired
    private SubscriptionService subscriptionServiceProxy;

    @MockitoBean
    private SubscriptionServiceImpl actualDbService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @MockitoBean
    private KafkaClient kafkaClient;

    @MockitoBean
    private KafkaAdmin kafkaAdmin;

    @Test
    void shouldCacheDataInValkeyInExpectedJsonFormat() {
        long chatId = 100L;
        String expectedCacheKey = "getLinks::" + chatId;
        ListLinksResponse mockResponse = new ListLinksResponse(List.of(), 0);

        when(actualDbService.findSubscriptionsWithLinks(chatId)).thenReturn(mockResponse);

        subscriptionServiceProxy.findSubscriptionsWithLinks(chatId);

        verify(actualDbService, times(1)).findSubscriptionsWithLinks(chatId);

        String jsonInValkey = redisTemplate.opsForValue().get(expectedCacheKey);

        assertNotNull(jsonInValkey, "Данные должны сохраниться в Valkey");

        assertTrue(jsonInValkey.contains("links"), "JSON должен содержать поле links");

        subscriptionServiceProxy.findSubscriptionsWithLinks(chatId);

        verify(actualDbService, times(1)).findSubscriptionsWithLinks(chatId);
    }

    @Test
    void shouldInvalidateCacheOnAddingNewLink() throws InterruptedException {
        long chatId = 200L;
        ListLinksResponse mockResponse = new ListLinksResponse(List.of(), 0);

        when(actualDbService.findSubscriptionsWithLinks(chatId)).thenReturn(mockResponse);

        subscriptionServiceProxy.findSubscriptionsWithLinks(chatId);

        subscriptionServiceProxy.findSubscriptionsWithLinks(chatId);

        verify(actualDbService, times(1)).findSubscriptionsWithLinks(chatId);

        AddLinkRequest addRequest = new AddLinkRequest("https://github.com", List.of());
        subscriptionServiceProxy.createSubscription(chatId, addRequest);

        Thread.sleep(500);

        subscriptionServiceProxy.findSubscriptionsWithLinks(chatId);

        verify(actualDbService, times(2)).findSubscriptionsWithLinks(chatId);
    }
}
