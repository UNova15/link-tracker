package backend.academy.linktracker.bot.kafka;

import backend.academy.linktracker.avro.LinkUpdateEvent;
import backend.academy.linktracker.bot.domain.NotificationDto;
import backend.academy.linktracker.bot.service.UpdateService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdatesListener {
    private final UpdateService updateService;

    @KafkaListener(topics = "${app.kafka.topic-name}", groupId = "telegram-bot-group")
    public void listen(@Payload LinkUpdateEvent linkUpdateEvent) {
        NotificationDto notification = new NotificationDto(
                linkUpdateEvent.getIdempotenceKey(),
                linkUpdateEvent.getLinkId(),
                linkUpdateEvent.getUrl(),
                linkUpdateEvent.getDescription(),
                linkUpdateEvent.getTgChatIds());

        updateService.sendUpdateMessage(notification);
    }
}
