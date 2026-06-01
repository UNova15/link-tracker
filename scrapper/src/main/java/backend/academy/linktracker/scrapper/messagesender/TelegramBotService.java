package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.domain.Notification;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TelegramBotService {
    private final TelegramBotClient telegramBotClient;

    @Retry(name = "bot")
    public void send(Notification notification) {
        telegramBotClient.send(notification);
    }
}
