package backend.academy.linktracker.bot.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import backend.academy.linktracker.avro.ProcessedLinkUpdate;
import backend.academy.linktracker.bot.dto.NotificationDto;
import backend.academy.linktracker.bot.properties.ScrapperProperties;
import backend.academy.linktracker.bot.service.UpdateService;
import com.pengrad.telegrambot.TelegramBot;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.kafka.KafkaContainer;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Import(KafkaConfiguration.class)
@EnableKafka
public class KafkaTest {

    static KafkaContainer kafkaContainer = new KafkaContainer("apache/kafka:4.3.0");

    @MockitoBean
    private UpdateService updateService;

    @MockitoBean
    private ScrapperProperties scrapperProperties;

    @MockitoBean
    private TelegramBot bot;

    @Autowired
    private KafkaTemplate<String, ProcessedLinkUpdate> kafka;

    @DynamicPropertySource
    static void settingProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @BeforeAll
    static void startKafka() {
        kafkaContainer.start();
    }

    @AfterAll
    static void stopAll() {
        kafkaContainer.close();
    }

    @Test
    void Kafka_pollKafka_getMessage() {
        ProcessedLinkUpdate linkUpdate = new ProcessedLinkUpdate(UUID.randomUUID(), List.of(1L), "New message", 1L);

        kafka.send("test-topic", linkUpdate);

        ArgumentCaptor<NotificationDto> captor = ArgumentCaptor.forClass(NotificationDto.class);
        verify(updateService, timeout(5000).times(1)).sendUpdateMessage(captor.capture());

        NotificationDto value = captor.getValue();
        assertThat(value.linksIds()).isEqualTo(linkUpdate.getLinksIds());
        assertThat(value.description()).isEqualTo(linkUpdate.getDescription());
        assertThat(value.tgChatId()).isEqualTo(linkUpdate.getTgChatId());
    }
}
