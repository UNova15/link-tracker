package backend.academy.linktracker.bot.client.scrapper;

import backend.academy.linktracker.bot.exception.ScrapperErrorHandler;
import backend.academy.linktracker.bot.model.AddLinkRequest;
import backend.academy.linktracker.bot.model.LinkResponse;
import backend.academy.linktracker.bot.model.ListLinkResponse;
import backend.academy.linktracker.bot.model.RemoveLinkRequest;
import backend.academy.linktracker.bot.properties.ScrapperClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ScrapperLinkClient {
    private final String linkEndpoint;
    private final RestClient scrapperClient;
    private final ScrapperErrorHandler errorHandler;

    @Autowired
    public ScrapperLinkClient(RestClient scrapperClient, ScrapperClientProperties properties, ScrapperErrorHandler errorHandler) {
        this.linkEndpoint = properties.getLinksEndpoint();
        this.scrapperClient = scrapperClient;
        this.errorHandler = errorHandler;
    }

    public ListLinkResponse getLinks(long chatId) {
        return scrapperClient.get()
            .uri(linkEndpoint)
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,errorHandler::handleScrapperClientError)
            .onStatus(HttpStatusCode::is5xxServerError,errorHandler::handleScrapperServerError)
            .body(ListLinkResponse.class);
    }

    public LinkResponse addLink(long chatId, AddLinkRequest req) {
        return scrapperClient.post()
            .uri(linkEndpoint)
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .body(req)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,errorHandler::handleScrapperClientError)
            .onStatus(HttpStatusCode::is5xxServerError,errorHandler::handleScrapperServerError)
            .body(LinkResponse.class);
    }

    public LinkResponse removeLink(long chatId, RemoveLinkRequest req) {
        return scrapperClient.method(HttpMethod.DELETE)
            .uri(linkEndpoint)
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .body(req)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,errorHandler::handleScrapperClientError)
            .onStatus(HttpStatusCode::is5xxServerError,errorHandler::handleScrapperServerError)
            .body(LinkResponse.class);
    }
}
