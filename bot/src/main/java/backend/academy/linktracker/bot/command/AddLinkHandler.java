package backend.academy.linktracker.bot.command;

import backend.academy.linktracker.bot.client.scrapper.ScrapperLinkClient;
import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.model.AddLinkRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddLinkHandler {
    private final ScrapperLinkClient scrapperLinkClient;

    @Autowired
    public AddLinkHandler(ScrapperLinkClient scrapperLinkClient) {
        this.scrapperLinkClient = scrapperLinkClient;
    }

    public SendMessage process(long chatId, SessionData sessionData) {
        AddLinkRequest request = new AddLinkRequest(sessionData.getLink(), sessionData.getTags());
        scrapperLinkClient.addLink(chatId, request);

        return new SendMessage(chatId,"Ссылка успешно сохранена");
    }
}
