package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.exception.handler.TelegramBotHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TelegramBotClient implements MessageSender {
    private final RestClient telegramClient;
    private final TelegramBotHandler telegramBotHandler;

    public TelegramBotClient(
            @Qualifier("telegramBotHttpClient") RestClient telegramClient, TelegramBotHandler telegramBotHandler) {
        this.telegramClient = telegramClient;
        this.telegramBotHandler = telegramBotHandler;
    }

    @Override
    public void send(LinkUpdate update) {
        telegramClient
                .post()
                .uri("/updates")
                .body(update)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, telegramBotHandler::handleTelegramError)
                .onStatus(HttpStatusCode::is5xxServerError, telegramBotHandler::handleTelegramError)
                .toBodilessEntity();
    }
}
