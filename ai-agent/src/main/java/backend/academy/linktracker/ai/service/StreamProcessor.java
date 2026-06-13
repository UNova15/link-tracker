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
import org.apache.kafka.streams.processor.api.ContextualProcessor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.state.WindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
@AllArgsConstructor
@Slf4j
public class StreamProcessor {
    private final MessageFilter filter;
    private final PrioritizeService prioritizeService;
    private final DtoValidator validator;

    private final NotificationMapper mapper;
    private final GroupingProperties groupingProperties;
    private final KafkaProperties kafkaProperties;

    @Bean
    public KStream<String, AggregatedNotification> buildStreamProcessor(
            StreamsBuilder builder,
            Serde<RawLinkUpdate> rawAvroSerde,
            Serde<AggregatedNotification> processedNotificationSerde,
            Serde<Notification> groupingSerde,
            Serde<List<Notification>> listNotificationsSerde) {

        StoreBuilder<WindowStore<String, Long>> dedupStoreBuilder = Stores.windowStoreBuilder(
                Stores.persistentWindowStore(
                        kafkaProperties.storeName(), kafkaProperties.ttl(), kafkaProperties.ttl(), false),
                Serdes.String(),
                Serdes.Long());

        builder.addStateStore(dedupStoreBuilder);

        KStream<String, AggregatedNotification> stream = builder.stream(
                        kafkaProperties.rawUpdatesTopic(), Consumed.with(Serdes.String(), rawAvroSerde))
                .peek((key, value) ->
                        log.debug("Доставлено сообщение в ai-agent-service. Начало агрегирования {}", value))

                // обработка повторного сообщения от scrapper с помощью RocksDB. Из за ключа - idempotencuKey в качестве
                // ключа kafka сообщения - дубликаты будут идти на те же самые инстансы ai-agent, если даже какой то из
                // них упадет то сообщение будет отправлено на инстанс, где уже kafka streams востановит копию локальной
                // бд из служебного топика
                .process(
                        () -> new ContextualProcessor<String, RawLinkUpdate, String, RawLinkUpdate>() {
                            private WindowStore<String, Long> dedupStore;

                            @Override
                            public void init(ProcessorContext<String, RawLinkUpdate> context) {
                                super.init(context);
                                this.dedupStore = context.getStateStore(kafkaProperties.storeName());
                            }

                            @Override
                            public void process(Record<String, RawLinkUpdate> record) {
                                String idempotencyKey = record.key();

                                long timeFrom = System.currentTimeMillis()
                                        - kafkaProperties.ttl().toMillis();
                                long timeTo = System.currentTimeMillis();

                                try (WindowStoreIterator<Long> iterator =
                                        dedupStore.fetch(idempotencyKey, timeFrom, timeTo)) {
                                    if (iterator.hasNext()) {
                                        return;
                                    }
                                }

                                dedupStore.put(idempotencyKey, 1L, System.currentTimeMillis());
                                context().forward(record);
                            }
                        },
                        kafkaProperties.storeName())
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
                // отключил для тестов - при реальной нагрузке включить
                // .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .map((key, value) -> new KeyValue<>(key.key(), new AggregatedNotification(value)));

        stream.to(kafkaProperties.aggregatedUpdatesTopic(), Produced.with(Serdes.String(), processedNotificationSerde));
        return stream;
    }
}
