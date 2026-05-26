package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.avro.LinkUpdateEvent;
import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import backend.academy.linktracker.scrapper.mapper.LinkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@ConditionalOnProperty(prefix = "app", name = "sender", havingValue = "mq", matchIfMissing = true)
@Slf4j
@RequiredArgsConstructor
public class KafkaClient implements MessageSender {

    private final KafkaTemplate<String, LinkUpdateEvent> kafka;
    private final LinkMapper linkMapper;

    @Value("${app.kafka.topic-name}")
    private String topicName;

    @Override
    public List<Long> send(List<NotificationRecord> records) {
        List<Long> updatedIds = Collections.synchronizedList(new ArrayList<>());

        // батчевая отправка сообщений в kafka
        List<CompletableFuture<Void>> events = records.stream()
                .map(record -> {
                    LinkUpdateEvent event = linkMapper.toLinkUpdateEvent(record);

                    return kafka.send(topicName, event.getUrl(), event)
                            .thenAccept(result -> updatedIds.add(record.id()))
                            .exceptionally(ex -> {
                                log.error("Error to send message: {} to broker ", event.getDescription());
                                return null;
                            });
                })
                .toList();

        // ожидание подтверждения доставки всех сообщений
        CompletableFuture.allOf(events.toArray(new CompletableFuture[0])).join();

        return updatedIds;
    }
}
