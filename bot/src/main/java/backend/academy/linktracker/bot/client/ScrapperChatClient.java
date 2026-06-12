package backend.academy.linktracker.bot.client;

import static backend.academy.linktracker.bot.configuration.ScrapperClientConfiguration.SCRAPPER_CONFIG_NAME;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/tg-chat/{id}")
@Retry(name = SCRAPPER_CONFIG_NAME)
@CircuitBreaker(name = SCRAPPER_CONFIG_NAME)
public interface ScrapperChatClient {

    @PostExchange
    void registrationChat(@PathVariable("id") long id);

    @DeleteExchange
    void removeChat(@PathVariable("id") long id);
}
