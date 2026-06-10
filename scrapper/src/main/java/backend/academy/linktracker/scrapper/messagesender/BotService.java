package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class BotService {
    private final BotSender bot;

    public List<Long> send(List<NotificationRecord> records) {
        List<Long> updatedIds = new ArrayList<>();

        for (var record : records) {
            try {
                bot.send(record.notification());
                updatedIds.add(record.id());
            } catch (Exception exception) {
                log.error(
                        "Ошибка в уведомлении пользователей об изменениях по ссылке: {}. {}",
                        record.notification().getUrl(),
                        exception.getMessage());
            }
        }
        return updatedIds;
    }
}
