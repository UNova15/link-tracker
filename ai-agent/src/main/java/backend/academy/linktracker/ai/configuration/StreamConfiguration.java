package backend.academy.linktracker.ai.configuration;

import backend.academy.linktracker.ai.dto.AggregatedNotification;
import backend.academy.linktracker.ai.dto.NotificationDto;
import backend.academy.linktracker.ai.mapper.NotificationMapper;
import backend.academy.linktracker.ai.properties.GroupingProperties;
import backend.academy.linktracker.ai.properties.KafkaProperties;
import backend.academy.linktracker.ai.service.MessageFilter;
import backend.academy.linktracker.ai.util.DtoValidator;
import backend.academy.linktracker.ai.util.StreamProcessingUtil;
import backend.academy.linktracker.avro.RawLinkUpdate;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JacksonJsonSerde;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableKafkaStreams
@AllArgsConstructor
public class StreamConfiguration {
    private final MessageFilter filter;
    private final NotificationMapper mapper;
    private final StreamProcessingUtil separator;
    private final KafkaProperties kafkaProperties;
    private final DtoValidator validator;
    private final GroupingProperties groupingProperties;

    @Bean
    public Serde<AggregatedNotification> aggregatedNotification() {
        return new JacksonJsonSerde<>(AggregatedNotification.class);
    }

    @Bean
    public Serde<NotificationDto> notificationDtoSerde() {
        return new JacksonJsonSerde<>(NotificationDto.class);
    }

    @Bean
    public Serde<RawLinkUpdate> rawLinkUpdateSerde(KafkaProperties properties) {
        SpecificAvroSerde<RawLinkUpdate> avroSerde = new SpecificAvroSerde<>();

        Map<String, String> config = Map.of("schema.registry.url", properties.schemaRegistryUrl());
        avroSerde.configure(config, false);
        return avroSerde;
    }

    @Bean
    public Serde<List<NotificationDto>> listSerde() {
        return new JacksonJsonSerde<>(new TypeReference<>() {}, new JsonMapper());
    }

    @Bean
    public KStream<String, AggregatedNotification> streamProcessor(
            StreamsBuilder builder,
            Serde<RawLinkUpdate> rawAvroSerde,
            Serde<AggregatedNotification> dtoSerde,
            Serde<NotificationDto> singleDroSerde,
            Serde<List<NotificationDto>> listSerde) {
        KStream<String, AggregatedNotification> stream = builder.stream(
                        kafkaProperties.rawUpdatesTopic(), Consumed.with(Serdes.String(), rawAvroSerde))
                .mapValues((key, value) -> mapper.toNotificationDto(value))
                .filter((key, value) -> validator.isValid(value))
                .filter((key, value) -> filter.filter(value))
                .flatMapValues((key, value) -> separator.split(value))
                .selectKey((key, value) -> String.valueOf(value.tgChatIds().getFirst()))
                .groupByKey(Grouped.with(Serdes.String(), singleDroSerde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(groupingProperties.windowMs())))
                .aggregate(
                        ArrayList::new,
                        (key, value, aggregate) -> {
                            aggregate.add(value);
                            return aggregate;
                        },
                        Materialized.with(Serdes.String(), listSerde))
                .toStream()
                .map((key, value) -> new KeyValue<>(key.key(), new AggregatedNotification(value)));

        stream.to(kafkaProperties.aggregatedUpdatesTopic(), Produced.with(Serdes.String(), dtoSerde));
        return stream;
    }
}
