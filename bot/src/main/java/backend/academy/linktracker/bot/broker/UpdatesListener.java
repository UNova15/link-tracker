package backend.academy.linktracker.bot.broker;

import backend.academy.linktracker.avro.ProcessedLinkUpdate;
import backend.academy.linktracker.bot.dto.NotificationDto;
import backend.academy.linktracker.bot.service.UpdateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UpdatesListener {
    private final UpdateService updateService;

    @KafkaListener(topics = "${app.kafka.topic-name}", groupId = "telegram-bot-group")
    public void listen(@Payload ProcessedLinkUpdate linkUpdateEvent) {
        NotificationDto notification = new NotificationDto(
                linkUpdateEvent.getIdempotenceKey(),
                linkUpdateEvent.getLinkId(),
                linkUpdateEvent.getDescription(),
                linkUpdateEvent.getTgChatIds());
        IO.println("ПОЛУЧЕНО СООБЩЕНИЕ: {}" + linkUpdateEvent.getDescription());
        updateService.sendUpdateMessage(notification);
    }
}
