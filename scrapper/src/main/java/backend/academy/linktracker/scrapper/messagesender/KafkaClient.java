package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.avro.LinkUpdateEvent;
import backend.academy.linktracker.scrapper.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app", name = "sender", havingValue = "mq", matchIfMissing = true)
@Slf4j
@RequiredArgsConstructor
public class KafkaClient implements MessageSender {

    private final KafkaTemplate<String, LinkUpdateEvent> kafka;

    @Value("${app.kafka.topic-name}")
    private String topicName;

    // TODO возможно плохая практика
    @Override
    @SneakyThrows
    public void send(Notification update) {
        LinkUpdateEvent linkUpdateEvent = new LinkUpdateEvent(
                update.getIdempotenceKey(),
                update.getLinkId(),
                update.getUrl(),
                update.getDescription(),
                update.getTgChatIds());

        kafka.send(topicName, linkUpdateEvent.getUrl(), linkUpdateEvent)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.error("Error to send message to Kafka: {}, exception: {}", update, ex.getMessage());
                    }
                })
                .get();
    }
}
