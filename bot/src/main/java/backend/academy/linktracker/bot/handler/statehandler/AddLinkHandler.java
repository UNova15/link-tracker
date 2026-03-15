package backend.academy.linktracker.bot.handler.statehandler;

import backend.academy.linktracker.bot.client.scrapper.ScrapperLinkClient;
import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.model.AddLinkRequest;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.util.TagParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AddLinkHandler implements Handler {
    private final ScrapperLinkClient scrapperLinkClient;
    private final TagParser parser;
    private final AwaitCommandState awaitCommandState;

    @Autowired
    public AddLinkHandler(ScrapperLinkClient scrapperLinkClient, TagParser parser, AwaitCommandState awaitCommandState) {
        this.scrapperLinkClient = scrapperLinkClient;
        this.parser = parser;
        this.awaitCommandState = awaitCommandState;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        List<String> tags = parser.parseTags(message.text());

        session.setTags(tags);
        session.setState(awaitCommandState);

        AddLinkRequest request = new AddLinkRequest(session.getLink(), session.getTags());
        scrapperLinkClient.addLink(message.id(), request);

        return "Ссылка успешно сохранена";
    }
}
