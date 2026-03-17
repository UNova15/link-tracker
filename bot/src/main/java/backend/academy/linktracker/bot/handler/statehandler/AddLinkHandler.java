package backend.academy.linktracker.bot.handler.statehandler;

import backend.academy.linktracker.bot.client.scrapper.ScrapperLinkClient;
import backend.academy.linktracker.bot.exception.ScrapperClientException;
import backend.academy.linktracker.bot.handler.StateChanger;
import backend.academy.linktracker.bot.model.AddLinkRequest;
import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AddLinkHandler extends StateChanger {
    private static final Logger logger = LoggerFactory.getLogger(AddLinkHandler.class);
    private final ScrapperLinkClient scrapperLinkClient;
    private final RequestArgsParser parser;

    @Autowired
    public AddLinkHandler(
            ScrapperLinkClient scrapperLinkClient,
            RequestArgsParser parser,
            @Lazy AwaitCommandState awaitCommandState) {
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
        } catch (ScrapperClientException exception) {
            logger.error(
                    "Ошибка отправки ссылки пользователя {}, {}. {}",
                    message.id(),
                    exception.getErrorResponse().description(),
                    exception.getErrorResponse().stackTrace());
            return "Ошибка сохранения ссылки: " + exception.getErrorResponse().exceptionMessage();
        }
    }
}
