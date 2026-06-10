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
public class BotSender {
    private final BotClient botClient;
    private final MessageBrokerClient messageBrokerClient;

    @Retry(name = "broker")
    @CircuitBreaker(name = "broker", fallbackMethod = "sendToHttpClient")
    public void send(Notification notification) {
        messageBrokerClient.send(notification);
    }

    @Retry(name = "bot")
    @CircuitBreaker(name = "bot")
    void sendToHttpClient(Notification notification, Throwable throwable) {
        log.error(
                "Ошибка отправки сообщения через Kafka. Отправка с помощью http client {}",
                throwable.getLocalizedMessage());
        botClient.send(notification);
    }
}
