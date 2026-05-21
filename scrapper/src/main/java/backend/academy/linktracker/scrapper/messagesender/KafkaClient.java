package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;
import lombok.RequiredArgsConstructor;
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

    private final KafkaTemplate<String, LinkUpdate> kafka;

    @Value("${app.topic-name}")
    private String topicName;

    @Override
    public void send(LinkUpdate update) {
        kafka.send(topicName, update.url(), update)
            .whenComplete((res, ex) -> {
                if (ex != null) {
                    log.error("Error to send message to Kafka: {}, exception: {}", update, ex.getMessage());
                }
            });

    }
}
