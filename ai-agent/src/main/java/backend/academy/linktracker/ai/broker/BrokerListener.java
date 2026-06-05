package backend.academy.linktracker.ai.broker;

import backend.academy.linktracker.ai.dto.NotificationDto;
import backend.academy.linktracker.ai.mapper.NotificationMapper;
import backend.academy.linktracker.ai.service.ProcessService;
import backend.academy.linktracker.avro.RawLinkUpdate;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BrokerListener {
    private final ProcessService service;
    private final NotificationMapper mapper;

    @KafkaListener(topics = "${app.kafka.raw-updates-topic}", id = "ai-agent-group")
    public void listen(@Payload RawLinkUpdate event) {
        NotificationDto notification = mapper.toNotificationDto(event);
        service.process(notification);
    }
}
