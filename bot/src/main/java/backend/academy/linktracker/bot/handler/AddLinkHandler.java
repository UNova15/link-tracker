package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.client.scrapper.ScrapperLinkClient;
import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.client.telegram.SessionStorage;
import backend.academy.linktracker.bot.handler.clientstatehandlers.AwaitCommandState;
import backend.academy.linktracker.bot.model.AddLinkRequest;
import backend.academy.linktracker.bot.util.TagParser;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class AddLinkHandler {
    private final ScrapperLinkClient scrapperLinkClient;
    private final SessionStorage storage;
    private final TagParser parser;
    private final AwaitCommandState awaitCommandState;

    @Autowired
    public AddLinkHandler(ScrapperLinkClient scrapperLinkClient, SessionStorage storage, TagParser parser, AwaitCommandState awaitCommandState) {
        this.scrapperLinkClient = scrapperLinkClient;
        this.parser = parser;
        this.storage = storage;
        this.awaitCommandState = awaitCommandState;
    }


    public SendMessage process(Update update) {
        long chatId = update.message().chat().id();
        String message = update.message().text();

        List<String> tags = parser.parseTags(message);
        SessionData session = storage.findSession(chatId);

        session.setTags(tags);
        session.setState(awaitCommandState);

        AddLinkRequest request = new AddLinkRequest(session.getLink(), session.getTags());
        scrapperLinkClient.addLink(chatId, request);

        return new SendMessage(chatId, "Ссылка успешно сохранена");
    }
}
