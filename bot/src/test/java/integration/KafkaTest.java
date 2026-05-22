package integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import backend.academy.linktracker.bot.dto.LinkUpdate;
import backend.academy.linktracker.bot.kafka.UpdatesListener;
import backend.academy.linktracker.bot.service.UpdateService;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.kafka.KafkaContainer;

@SpringBootTest(
        classes = {UpdatesListener.class},
        properties = {
            "app.kafka.topic-name=test-topic",
            "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
            "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer",
            "spring.kafka.consumer.group-id=test-group",
        })
@ImportAutoConfiguration(KafkaAutoConfiguration.class)
@Import(KafkaConfiguration.class)
@EnableKafka
public class KafkaTest {

    static KafkaContainer kafkaContainer = new KafkaContainer("apache/kafka:4.3.0");

    @MockitoBean
    private UpdateService updateService;

    @Autowired
    private KafkaTemplate<String, LinkUpdate> kafka;

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
        LinkUpdate linkUpdate = new LinkUpdate(1, "https://github.com", "New message", List.of(1L));

        kafka.send("test-topic", linkUpdate);

        ArgumentCaptor<LinkUpdate> captor = ArgumentCaptor.forClass(LinkUpdate.class);
        verify(updateService, timeout(5000).times(1)).sendUpdateMessage(captor.capture());

        LinkUpdate value = captor.getValue();
        assertThat(value.id()).isEqualTo(linkUpdate.id());
        assertThat(value.description()).isEqualTo(linkUpdate.description());
        assertThat(value.url()).isEqualTo(linkUpdate.url());
        assertThat(value.tgChatIds()).isEqualTo(linkUpdate.tgChatIds());
    }
}
