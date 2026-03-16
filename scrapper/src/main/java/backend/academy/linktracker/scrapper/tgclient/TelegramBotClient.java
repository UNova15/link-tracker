package backend.academy.linktracker.scrapper.tgclient;

import backend.academy.linktracker.scrapper.exception.handler.TelegramBotHandler;
import backend.academy.linktracker.scrapper.model.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.properties.TelegramBotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TelegramBotClient {
    private final RestClient telegramClient;
    private final String updateEndpoint;
    private final TelegramBotHandler telegramBotHandler;

    @Autowired
    public TelegramBotClient(@Qualifier("telegramBotHttpClient") RestClient telegramClient, TelegramBotProperties properties, TelegramBotHandler telegramBotHandler) {
        this.telegramClient = telegramClient;
        this.updateEndpoint = properties.getUpdateEndpoint();
        this.telegramBotHandler = telegramBotHandler;
    }

    public void sendUpdateRequest(LinkUpdate update) {
        telegramClient.post()
            .uri(updateEndpoint)
            .body(update)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, telegramBotHandler::handleTelegramError)
            .onStatus(HttpStatusCode::is5xxServerError, telegramBotHandler::handleTelegramError)
            .toBodilessEntity();
    }
}
