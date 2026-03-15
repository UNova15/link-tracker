package backend.academy.linktracker.bot.handler.statehandler;

import backend.academy.linktracker.bot.client.scrapper.ScrapperLinkClient;
import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.exception.ScrapperException;
import backend.academy.linktracker.bot.handler.StateChanger;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.model.AddLinkRequest;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.util.TagParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AddLinkHandler extends StateChanger {
    private static final Logger logger = LoggerFactory.getLogger(AddLinkHandler.class);
    private final ScrapperLinkClient scrapperLinkClient;
    private final TagParser parser;

    @Autowired
    public AddLinkHandler(ScrapperLinkClient scrapperLinkClient, TagParser parser, @Lazy AwaitCommandState awaitCommandState) {
        super(awaitCommandState);
        this.scrapperLinkClient = scrapperLinkClient;
        this.parser = parser;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        try {
            List<String> tags = parser.parseTags(message.text());

            session.setTags(tags);
            AddLinkRequest request = new AddLinkRequest(session.getLink(), session.getTags());
            scrapperLinkClient.addLink(message.id(), request);

            changeState(session);
            return "Ссылка успешно сохранена";
        } catch (ScrapperException exception) {
            logger.warn("Ошибка отправки ссылки пользователя {}, {}", message.id(), exception.getStack());
            return "Ошибка сохранения ссылки: " + exception.getDescription() + exception.getStack();
        }
    }
}
