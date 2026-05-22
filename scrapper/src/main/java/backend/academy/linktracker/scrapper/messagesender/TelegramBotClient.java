package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.exception.handler.TelegramBotExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

//TODO тоже переписать в декларативном стиле
@Component
@ConditionalOnProperty(prefix = "app",name = "sender", havingValue = "rest")
public class TelegramBotClient implements MessageSender {
    private final RestClient telegramClient;
    private final TelegramBotExceptionHandler telegramBotExceptionHandler;

    public TelegramBotClient(
            @Qualifier("telegramBotHttpClient") RestClient telegramClient, TelegramBotExceptionHandler telegramBotExceptionHandler) {
        this.telegramClient = telegramClient;
        this.telegramBotExceptionHandler = telegramBotExceptionHandler;
    }

    @Override
    public void send(LinkUpdate update) {
        telegramClient
                .post()
                .uri("/updates")
                .body(update)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, telegramBotExceptionHandler::handleTelegramError)
                .onStatus(HttpStatusCode::is5xxServerError, telegramBotExceptionHandler::handleTelegramError)
                .toBodilessEntity();
    }
}
