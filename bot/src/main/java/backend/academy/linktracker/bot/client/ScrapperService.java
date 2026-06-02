package backend.academy.linktracker.bot.client;

import backend.academy.linktracker.bot.dto.AddLinkRequest;
import backend.academy.linktracker.bot.dto.LinkResponse;
import backend.academy.linktracker.bot.dto.ListLinkResponse;
import backend.academy.linktracker.bot.dto.RemoveLinkRequest;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScrapperService {
    private final ScrapperChatClient chatClient;
    private final ScrapperLinkClient linkClient;

    @Retry(name = "scrapper")
    public ListLinkResponse getLinks(long id) {
        return linkClient.getLinks(id);
    }

    @Retry(name = "scrapper")
    public LinkResponse addLink(long id, AddLinkRequest req) {
        return linkClient.addLink(id, req);
    }

    @Retry(name = "scrapper")
    public LinkResponse removeLink(long id, RemoveLinkRequest req) {
        return linkClient.removeLink(id, req);
    }

    @Retry(name = "scrapper")
    public void registrationChat(long id) {
        chatClient.registrationChat(id);
    }

    @Retry(name = "scrapper")
    public void removeChat(long id) {
        chatClient.removeChat(id);
    }
}
