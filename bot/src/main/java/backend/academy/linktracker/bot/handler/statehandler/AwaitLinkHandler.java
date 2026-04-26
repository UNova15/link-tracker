package backend.academy.linktracker.bot.handler.statehandler;

import backend.academy.linktracker.bot.handler.StateChanger;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.state.AwaitTegState;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AwaitLinkHandler extends StateChanger {
    private static final String MESSAGE = "Введите через запятую теги для ссылки";

    public AwaitLinkHandler(@Lazy AwaitTegState awaitTegState) {
        super(awaitTegState);
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        session.setLink(message.text());
        changeState(session);
        return MESSAGE;
    }
}
