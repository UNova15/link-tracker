package backend.academy.linktracker.scrapper.tgclient;

import backend.academy.linktracker.scrapper.exception.ApiErrorResponse;
import backend.academy.linktracker.scrapper.model.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.properties.TelegramBotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TelegramBotClient {
    private final RestClient telegramClient;
    private final String updateEndpoint;

    @Autowired
    public TelegramBotClient(@Qualifier("telegramBotHttpClient") RestClient telegramClient, TelegramBotProperties properties) {
        this.telegramClient = telegramClient;
        this.updateEndpoint = properties.getUpdateEndpoint();
    }

    //TODO обработка ответа?
    public void sendUpdateRequest(LinkUpdate update) {
        telegramClient.post()
            .uri(updateEndpoint)
            .body(update)
            .retrieve()
            .body(ApiErrorResponse.class);
    }
}
