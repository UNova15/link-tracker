package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.avro.RawLinkUpdate;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.exception.MessageBrokerException;
import backend.academy.linktracker.scrapper.mapper.NotificationMapper;
import backend.academy.linktracker.scrapper.properties.KafkaProperties;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageBrokerClient {
    public static final String BROKER_CONFIG_NAME = "broker";

    private final KafkaTemplate<String, RawLinkUpdate> kafka;
    private final KafkaProperties properties;
    private final NotificationMapper mapper;

    @Retry(name = BROKER_CONFIG_NAME)
    public void send(Notification record) {
        RawLinkUpdate event = mapper.toLinkUpdateEvent(record);
        try {
            kafka.send(properties.topicName(), event.getUrl(), event)
                    .get(properties.timeoutSeconds(), TimeUnit.SECONDS);
        } catch (Exception exception) {
            log.error("Error to send message: {} to broker ", event.getDescription());
            throw new MessageBrokerException(exception);
        }
    }
}
