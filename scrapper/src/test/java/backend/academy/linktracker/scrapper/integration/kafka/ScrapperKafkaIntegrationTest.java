package backend.academy.linktracker.scrapper.integration.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.messagesender.KafkaClient;
import backend.academy.linktracker.scrapper.properties.KafkaConfiguration;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.KafkaContainer;

@SpringBootTest(
        classes = {KafkaClient.class, KafkaConfiguration.class},
        properties = {
            "app.db-provider=sql",
            "app.sender=mq",
            "app.kafka.topic-name=test-link-updates",
            "app.kafka.replicas=1",
            "app.kafka.partitions=1",
        })
@ImportAutoConfiguration(KafkaAutoConfiguration.class)
public class ScrapperKafkaIntegrationTest {

    @Autowired
    private KafkaClient kafkaClient;

    static KafkaContainer kafka = new KafkaContainer("apache/kafka:4.3.0");

    @DynamicPropertySource
    static void setKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @BeforeAll
    static void startKafka() {
        kafka.start();
    }

    @AfterAll
    static void closeKafka() {
        kafka.close();
    }

    @Test
    public void Kafka_sendValidMessage_saveMessageInKafka() {
        LinkUpdate linkUpdate = new LinkUpdate(1, "https:/guthub.com", "New Update", List.of(1L, 2L));

        KafkaConsumer<String, LinkUpdate> consumer = new KafkaConsumer<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafka.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG,
                "test-group",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JacksonJsonDeserializer.class,
                "spring.json.trusted.packages",
                "*",
                "spring.json.value.default.type",
                LinkUpdate.class));

        consumer.subscribe(List.of("test-link-updates"));

        kafkaClient.send(linkUpdate);

        ConsumerRecords<String, LinkUpdate> records = consumer.poll(Duration.ofSeconds(10));

        assertThat(records.count()).isEqualTo(1);

        ConsumerRecord<String, LinkUpdate> record = records.iterator().next();
        LinkUpdate value = record.value();

        assertThat(value.id()).isEqualTo(1);
        assertThat(value.description()).isEqualTo("New Update");
        assertThat(value.url()).isEqualTo("https:/guthub.com");
        assertThat(value.tgChatIds()).isEqualTo(List.of(1L, 2L));
    }
}
