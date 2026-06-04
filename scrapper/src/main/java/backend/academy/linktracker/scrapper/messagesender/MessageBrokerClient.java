package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.avro.LinkUpdateEvent;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.exception.MessageBrokerException;
import backend.academy.linktracker.scrapper.mapper.NotificationMapper;
import backend.academy.linktracker.scrapper.properties.KafkaProperties;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageBrokerClient {
    private final KafkaTemplate<String, LinkUpdateEvent> kafka;
    private final KafkaProperties properties;
    private final NotificationMapper mapper;

    public void send(Notification record) {
        LinkUpdateEvent event = mapper.toLinkUpdateEvent(record);
        try {
            kafka.send(properties.topicName(), event.getUrl(), event).get(properties.timeout(), TimeUnit.SECONDS);
        } catch (Exception exception) {
            log.error("Error to send message: {} to broker ", event.getDescription());
            throw new MessageBrokerException(exception);
        }
    }
}
