package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.domain.Notification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/updates")
public interface TelegramBotClient {

    @PostExchange
    void send(@RequestBody Notification notification);
}
