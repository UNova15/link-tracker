package backend.academy.linktracker.ai.configuration;

import backend.academy.linktracker.ai.domain.Notification;
import backend.academy.linktracker.ai.dto.AggregatedNotification;
import backend.academy.linktracker.ai.properties.KafkaProperties;
import backend.academy.linktracker.avro.RawLinkUpdate;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JacksonJsonSerde;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@AllArgsConstructor
public class StreamConfiguration {

    @Bean
    public Serde<AggregatedNotification> aggregatedNotification() {
        return new JacksonJsonSerde<>(AggregatedNotification.class);
    }

    @Bean
    public Serde<Notification> notificationDtoSerde() {
        return new JacksonJsonSerde<>(Notification.class);
    }

    @Bean
    public Serde<RawLinkUpdate> rawLinkUpdateSerde(KafkaProperties properties) {
        SpecificAvroSerde<RawLinkUpdate> avroSerde = new SpecificAvroSerde<>();

        Map<String, String> config = Map.of("schema.registry.url", properties.schemaRegistryUrl());
        avroSerde.configure(config, false);
        return avroSerde;
    }

    @Bean
    public Serde<List<Notification>> listSerde() {
        return new JacksonJsonSerde<>(new TypeReference<>() {}, new JsonMapper());
    }
}
