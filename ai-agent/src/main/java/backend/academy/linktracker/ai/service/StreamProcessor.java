package backend.academy.linktracker.ai.service;

import backend.academy.linktracker.ai.domain.Notification;
import backend.academy.linktracker.ai.dto.AggregatedNotification;
import backend.academy.linktracker.ai.mapper.NotificationMapper;
import backend.academy.linktracker.ai.properties.GroupingProperties;
import backend.academy.linktracker.ai.properties.KafkaProperties;
import backend.academy.linktracker.ai.util.DtoValidator;
import backend.academy.linktracker.avro.RawLinkUpdate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Configuration
@EnableKafkaStreams
@AllArgsConstructor
@Slf4j
public class StreamProcessor {
    private final MessageFilter filter;
    private final NotificationMapper mapper;
    private final KafkaProperties kafkaProperties;
    private final DtoValidator validator;
    private final GroupingProperties groupingProperties;
    private final PrioritizeService prioritizeService;

    @Bean
    public KStream<String, AggregatedNotification> buildStreamProcessor(
            StreamsBuilder builder,
            Serde<RawLinkUpdate> rawAvroSerde,
            Serde<AggregatedNotification> processedNotificationSerde,
            Serde<Notification> groupingSerde,
            Serde<List<Notification>> listNotificationsSerde) {

        KStream<String, AggregatedNotification> stream = builder.stream(
                        kafkaProperties.rawUpdatesTopic(), Consumed.with(Serdes.String(), rawAvroSerde))
                .peek((key, value) ->
                        log.debug("Доставлено сообщение в ai-agent-service. Начало агрегирования {}", value))
                .mapValues((key, value) -> mapper.toNotificationDto(value))
                .filter((key, value) -> validator.isValid(value))
                .flatMapValues((key, value) -> mapper.toNotificationList(value))
                .filter((key, value) -> filter.filter(value))
                .mapValues((key, value) -> value.withPriority(prioritizeService.prioritize(value)))
                .selectKey((key, value) -> String.valueOf(value.tgChatId()))
                .groupByKey(Grouped.with(Serdes.String(), groupingSerde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(groupingProperties.windowMs()))
                .aggregate(
                        ArrayList::new,
                        (key, value, aggregate) -> {
                            aggregate.add(value);
                            return aggregate;
                        },
                        Materialized.with(Serdes.String(), listNotificationsSerde))
                .toStream()
                .map((key, value) -> new KeyValue<>(key.key(), new AggregatedNotification(value)));

        stream.to(kafkaProperties.aggregatedUpdatesTopic(), Produced.with(Serdes.String(), processedNotificationSerde));
        return stream;
    }
}
