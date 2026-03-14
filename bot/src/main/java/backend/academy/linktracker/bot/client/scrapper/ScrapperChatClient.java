package backend.academy.linktracker.bot.client.scrapper;

import backend.academy.linktracker.bot.properties.ScrapperClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ScrapperChatClient {
    private final String chatRegistrationEndpoint;
    private final RestClient scrapperClient;

    @Autowired
    public ScrapperChatClient(RestClient scrapperClient, ScrapperClientProperties properties) {
        this.scrapperClient = scrapperClient;
        this.chatRegistrationEndpoint = properties.getChatEndpoint();
    }

    //TODO обработка исключений
    public void registrationChat(long chatId) {
        scrapperClient.post()
            .uri(chatRegistrationEndpoint + chatId)
            .retrieve();
    }

    public void removeChat(long chatId) {
        scrapperClient.delete()
            .uri(chatRegistrationEndpoint + chatId)
            .retrieve();
    }
}
