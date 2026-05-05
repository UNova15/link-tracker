package backend.academy.linktracker.bot.handler.statehandler;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.StateChanger;
import backend.academy.linktracker.bot.state.AwaitTagState;
import backend.academy.linktracker.bot.validator.LinkValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AwaitLinkHandler extends StateChanger {
    private static final String MESSAGE = "Введите через запятую теги для ссылки";

    private final LinkValidator linkValidator;

    public AwaitLinkHandler(@Lazy AwaitTagState awaitTagState, LinkValidator linkValidator) {
        super(awaitTagState);
        this.linkValidator = linkValidator;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        String uri = message.text().trim();

        return linkValidator.validate(uri).orElseGet(() -> {
            session.setLink(uri);
            changeState(session);
            return MESSAGE;
        });
    }
}
