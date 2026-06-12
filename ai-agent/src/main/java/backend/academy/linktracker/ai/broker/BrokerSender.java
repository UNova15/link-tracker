package backend.academy.linktracker.ai.broker;

import backend.academy.linktracker.ai.properties.KafkaProperties;
import backend.academy.linktracker.avro.ProcessedLinkUpdate;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BrokerSender {
    public static final String BROKER_CONFIG = "broker";

    private final KafkaProperties properties;
    private final KafkaTemplate<String, ProcessedLinkUpdate> kafka;

    @Retry(name = BROKER_CONFIG)
    public void sendNotification(ProcessedLinkUpdate update) {
        log.debug("Сообщение обработано и готово к отправке {}", update);

        try {
            kafka.send(properties.processedUpdatesTopic(), update).get(properties.timeoutSeconds(), TimeUnit.SECONDS);
        } catch (Exception exception) {
            log.error("Ошибка отправки сообщения в kafka {}", update);
            throw new KafkaException(exception.getMessage());
        }
    }
}
