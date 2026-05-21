package backend.academy.linktracker.bot.kafka;

import backend.academy.linktracker.bot.dto.LinkUpdate;
import backend.academy.linktracker.bot.service.UpdateService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdatesListener {
    private final UpdateService updateService;

    @KafkaListener(topics = "${app.topic-name}",groupId = "telegram-bot-group")
    public void listen(LinkUpdate linkUpdate){
        updateService.sendUpdateMessage(linkUpdate);
    }
}
