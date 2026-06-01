package backend.academy.linktracker.bot.scrapperclient;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/tg-chat/{id}")
public interface ScrapperChatClient {

    @PostExchange
    void registrationChat(@PathVariable("id") long id);

    @DeleteExchange
    void removeChat(@PathVariable("id") long id);
}
