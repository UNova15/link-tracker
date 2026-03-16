package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.scrapper.ScrapperLinkClient;
import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.exception.ScrapperClientException;
import backend.academy.linktracker.bot.model.Command;
import backend.academy.linktracker.bot.model.RemoveLinkRequest;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class UntrackHandler extends CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(UntrackHandler.class);

    private final ScrapperLinkClient scrapperClient;
    private final RequestArgsParser parser;

    @Autowired
    public UntrackHandler(@Lazy AwaitCommandState newState, ScrapperLinkClient client, RequestArgsParser parser) {
        super(new Command("/untrack", "Прекращение отслеживания ссылки"), newState);
        this.scrapperClient = client;
        this.parser = parser;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        String link = parser.parseRemoveLink(message.text());

        RemoveLinkRequest request = new RemoveLinkRequest(link);

        try {
            scrapperClient.removeLink(message.id(), request);
            changeState(session);
            return "Ссылка успешно удалена";
        } catch (ScrapperClientException exception) {
            logger.error("Ошибка удаления ссылки {} пользователя {}. {}", message.text(), message.id(),
                exception.getErrorResponse().stackTrace());
            return "Ошибка удаления ссылки";
        }
    }
}
