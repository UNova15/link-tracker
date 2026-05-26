package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import backend.academy.linktracker.scrapper.exception.TelegramBotException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "app", name = "sender", havingValue = "http")
@AllArgsConstructor
@Slf4j
public class HttpSender implements MessageSender {
    private final TelegramBotClient client;

    @Override
    public List<Long> send(List<NotificationRecord> records) {
        List<Long> updatedIds = new ArrayList<>();

        for (var record : records) {
            try {
                client.send(record.notification());
                updatedIds.add(record.notification().getLinkId());
            } catch (TelegramBotException exception) {
                log.error(
                        "Ошибка в уведомлении пользователей об изменениях по ссылке: {}. {}",
                        record.notification().getUrl(),
                        exception.getApiErrorResponse().description());
            }
        }
        return updatedIds;
    }
}
