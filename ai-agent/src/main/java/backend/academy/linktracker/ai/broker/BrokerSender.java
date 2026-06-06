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

    public void sendNotification(ProcessedLinkUpdate update) {
        String key = String.valueOf(update.getLinkId());

        try {
            kafka.send(topicName, key, update);
            IO.println("СООБЩЕНИЕ ОТПРАВЛЕНО В BOT");
        } catch (Exception exception) {
            log.error("Ошибка отправки сообщения в очередь сообщений");
        }
    }
}
