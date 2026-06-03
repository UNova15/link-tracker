package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.domain.Notification;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TelegramBotService {
    private final TelegramBotClient telegramBotClient;
    private final MessageBrokerClient messageBrokerClient;

    @Retry(name = "bot")
    @CircuitBreaker(name = "bot", fallbackMethod = "sendToBroker")
    public void send(Notification notification) {
        telegramBotClient.send(notification);
    }

    void sendToBroker(Notification notification, Throwable exception) {
        log.warn(
                "Ошибка при отправке уведомления по HTTP. Отправка в очередь сообщений: {}, {}",
                notification.getUrl(),
                exception.getMessage());
        messageBrokerClient.send(notification);
    }
}
