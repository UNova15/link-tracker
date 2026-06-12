package backend.academy.linktracker.scrapper.messagesender;

import static backend.academy.linktracker.scrapper.messagesender.MessageBrokerClient.BROKER_CONFIG_NAME;

import backend.academy.linktracker.scrapper.domain.Notification;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BotSender {
    private final BotClient botClient;
    private final MessageBrokerClient messageBrokerClient;

    @CircuitBreaker(name = BROKER_CONFIG_NAME, fallbackMethod = "sendToHttpClient")
    public void send(Notification notification) {
        messageBrokerClient.send(notification);
    }

    void sendToHttpClient(Notification notification, Throwable throwable) {
        log.error(
                "Ошибка отправки сообщения через Kafka. Отправка с помощью http client {}",
                throwable.getLocalizedMessage());
        botClient.send(notification);
    }
}
