package backend.academy.linktracker.bot.client.scrapper;

import backend.academy.linktracker.bot.model.AddLinkRequest;
import backend.academy.linktracker.bot.model.LinkResponse;
import backend.academy.linktracker.bot.model.ListLinkResponse;
import backend.academy.linktracker.bot.model.RemoveLinkRequest;
import backend.academy.linktracker.bot.properties.ScrapperClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ScrapperLinkClient {
    private final String linkEndpoint;
    private final RestClient scrapperClient;

    @Autowired
    public ScrapperLinkClient(RestClient scrapperClient, ScrapperClientProperties properties) {
        this.linkEndpoint = properties.getLinksEndpoint();
        this.scrapperClient = scrapperClient;
    }

    public ListLinkResponse getLinks(long chatId) {
        return scrapperClient.get()
            .uri(linkEndpoint)
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .retrieve()
            .body(ListLinkResponse.class);
    }

    public LinkResponse addLink(long chatId, AddLinkRequest request) {
        return scrapperClient.post()
            .uri(linkEndpoint)
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .body(request)
            .retrieve()
            .body(LinkResponse.class);
    }

    public LinkResponse removeLink(long chatId, RemoveLinkRequest request){
        return scrapperClient.method(HttpMethod.DELETE)
            .uri(linkEndpoint)
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .body(request)
            .retrieve()
            .body(LinkResponse.class);
    }
}
