package backend.academy.linktracker.ai.broker;

import backend.academy.linktracker.avro.ProcessedLinkUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BrokerSender {
    @Value("${app.kafka.processed-updates-topic}")
    private String topicName;

    private final KafkaTemplate<String, ProcessedLinkUpdate> kafka;

    // TODO fix it
    public void sendNotification(ProcessedLinkUpdate update) {
        log.debug("Сообщение обработано и готово к отправке {}", update);

        kafka.send(topicName, update).exceptionally(exception -> {
            log.error("Ошибка отправки сообщения в очередь сообщений {}", exception.getMessage());
            return null;
        });
    }
}
