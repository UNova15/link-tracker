package backend.academy.linktracker.bot.messagebroker;

import backend.academy.linktracker.avro.ProcessedLinkUpdate;
import backend.academy.linktracker.bot.dto.NotificationDto;
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
    public void listen(@Payload ProcessedLinkUpdate linkUpdateEvent) {
        NotificationDto notification = new NotificationDto(
                linkUpdateEvent.getIdempotenceKey(),
                linkUpdateEvent.getLinkId(),
                linkUpdateEvent.getDescription(),
                linkUpdateEvent.getTgChatIds());

        updateService.sendUpdateMessage(notification);
    }
}
