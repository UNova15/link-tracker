package backend.academy.linktracker.bot.kafka;

import backend.academy.linktracker.bot.dto.LinkUpdate;
import backend.academy.linktracker.bot.service.UpdateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdatesListener {
    private final UpdateService updateService;

    @KafkaListener(topics = "${app.kafka.topic-name}", groupId = "telegram-bot-group")
    public void listen(@Payload @Valid LinkUpdate linkUpdate) {
        updateService.sendUpdateMessage(linkUpdate);
    }
}
