package backend.academy.linktracker.bot.kafka;

import backend.academy.linktracker.avro.LinkUpdateEvent;
import backend.academy.linktracker.bot.dto.LinkUpdate;
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
        LinkUpdate linkUpdate = new LinkUpdate(
                linkUpdateEvent.getId(),
                linkUpdateEvent.getUrl(),
                linkUpdateEvent.getDescription(),
                linkUpdateEvent.getTgChatIds());

        updateService.sendUpdateMessage(linkUpdate);
    }
}
