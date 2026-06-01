package backend.academy.linktracker.scrapper.integration.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.linktracker.avro.LinkUpdateEvent;
import backend.academy.linktracker.scrapper.configuration.KafkaConfiguration;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import backend.academy.linktracker.scrapper.mapper.LinkMapper;
import backend.academy.linktracker.scrapper.messagesender.KafkaSender;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.kafka.KafkaContainer;

@SpringBootTest(
        classes = {KafkaSender.class, KafkaConfiguration.class, LinkMapper.class},
        properties = {
            "app.db-provider=sql",
            "app.sender=mq",
            "app.kafka.topic-name=test-link-updates",
            "app.kafka.replicas=1",
            "app.kafka.partitions=1",
            "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
            "spring.kafka.producer.value-serializer=io.confluent.kafka.serializers.KafkaAvroSerializer",
            "spring.kafka.producer.properties.schema.registry.url=mock://test-registry"
        })
@ImportAutoConfiguration(KafkaAutoConfiguration.class)
public class ScrapperKafkaIntegrationTest {

    @Autowired
    private KafkaSender kafkaSender;

    @Container
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
        List<NotificationRecord> notification = List.of(new NotificationRecord(
                1L, Notification.createNew(UUID.randomUUID(), 1L, "https:/guthub.com", "New Update", List.of(1L, 2L))));

        KafkaConsumer<String, LinkUpdateEvent> consumer = new KafkaConsumer<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafka.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG,
                "test-group",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                KafkaAvroDeserializer.class,
                "schema.registry.url",
                "mock://test-registry",
                "specific.avro.reader",
                "true"));

        consumer.subscribe(List.of("test-link-updates"));

        kafkaSender.send(notification);

        ConsumerRecords<String, LinkUpdateEvent> records = consumer.poll(Duration.ofSeconds(10));

        assertThat(records.count()).isEqualTo(1);

        ConsumerRecord<String, LinkUpdateEvent> record = records.iterator().next();
        LinkUpdateEvent value = record.value();

        assertThat(value.getLinkId()).isEqualTo(1);
        assertThat(value.getDescription()).isEqualTo("New Update");
        assertThat(value.getUrl()).isEqualTo("https:/guthub.com");
        assertThat(value.getTgChatIds()).isEqualTo(List.of(1L, 2L));
    }
}
