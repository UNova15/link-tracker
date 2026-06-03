package backend.academy.linktracker.bot.handler.statehandler;

import backend.academy.linktracker.bot.client.ScrapperService;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.dto.AddLinkRequest;
import backend.academy.linktracker.bot.handler.StateChanger;
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
    private static final String ERROR_MESSAGE = "Ошибка сохранения ссылки";

    private final ScrapperService scrapper;
    private final RequestArgsParser parser;

    public AddLinkHandler(
            ScrapperService scrapper, RequestArgsParser parser, @Lazy AwaitCommandState awaitCommandState) {
        super(awaitCommandState);
        this.scrapper = scrapper;
        this.parser = parser;
    }

    @Override
    protected void changeState(SessionData session) {
        session.setState(newState);
        session.setLink(null);
        session.setTags(null);
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        try {
            List<String> tags = parser.parseTags(message.text());

            session.setTags(tags);
            AddLinkRequest request = new AddLinkRequest(session.getLink(), session.getTags());
            scrapper.addLink(message.id(), request);

            changeState(session);
            return SUCCESS_MESSAGE;
        } catch (Exception exception) {
            log.error("Ошибка отправки ссылки пользователя {}, {}", message.id(), exception.getMessage());
            changeState(session);
            return ERROR_MESSAGE;
        }
    }
}
