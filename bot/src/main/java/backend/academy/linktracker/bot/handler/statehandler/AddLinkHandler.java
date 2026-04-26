package backend.academy.linktracker.bot.handler.statehandler;

import backend.academy.linktracker.bot.client.scrapper.ScrapperLinkClient;
import backend.academy.linktracker.bot.exception.ScrapperClientException;
import backend.academy.linktracker.bot.handler.StateChanger;
import backend.academy.linktracker.bot.dto.AddLinkRequest;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddLinkHandler extends StateChanger {
    private static final String SUCCESS_MESSAGE = "Ссылка успешно сохранена";
    private static final String ERROR_MESSAGE = "Ошибка сохранения ссылки: ";

    private final ScrapperLinkClient scrapperLinkClient;
    private final RequestArgsParser parser;

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
            return SUCCESS_MESSAGE;
        } catch (ScrapperClientException exception) {
            log.error(
                    "Ошибка отправки ссылки пользователя {}, {}. {}",
                    message.id(),
                    exception.getErrorResponse().description(),
                    exception.getErrorResponse().stackTrace());
            return ERROR_MESSAGE + exception.getErrorResponse().exceptionMessage();
        }
    }
}
