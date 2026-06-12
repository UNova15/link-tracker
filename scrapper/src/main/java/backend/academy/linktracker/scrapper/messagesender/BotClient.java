package backend.academy.linktracker.scrapper.messagesender;

import static backend.academy.linktracker.scrapper.configuration.HttpClientConfiguration.BOT_CONFIG_NAME;

import backend.academy.linktracker.scrapper.domain.Notification;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/updates")
@Retry(name = BOT_CONFIG_NAME)
@CircuitBreaker(name = BOT_CONFIG_NAME)
public interface BotClient {

    @PostExchange
    void send(@RequestBody Notification notification);
}
