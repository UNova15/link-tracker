package backend.academy.linktracker.bot.client.scrapper;

import backend.academy.linktracker.bot.exception.ScrapperErrorHandler;
import backend.academy.linktracker.bot.properties.ScrapperClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ScrapperChatClient {
    private final ScrapperErrorHandler errorHandler;
    private final String chatRegistrationEndpoint;
    private final RestClient scrapperClient;

    @Autowired
    public ScrapperChatClient(RestClient scrapperClient, ScrapperClientProperties properties, ScrapperErrorHandler errorHandler) {
        this.scrapperClient = scrapperClient;
        this.chatRegistrationEndpoint = properties.getChatEndpoint();
        this.errorHandler = errorHandler;
    }

    public void registrationChat(long chatId) {
        scrapperClient.post()
            .uri(chatRegistrationEndpoint + chatId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,errorHandler::handleScrapperClientError)
            .onStatus(HttpStatusCode::is5xxServerError,errorHandler::handleScrapperClientError)
            .toBodilessEntity();
    }

    public void removeChat(long chatId) {
        scrapperClient.delete()
            .uri(chatRegistrationEndpoint + chatId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,errorHandler::handleScrapperClientError)
            .onStatus(HttpStatusCode::is5xxServerError,errorHandler::handleScrapperClientError)
            .toBodilessEntity();
    }
}
