package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/updates")
public interface TelegramBotClient extends MessageSender {

    @Override
    @PostExchange
    void send(@RequestBody LinkUpdate update);
}
