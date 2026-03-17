package backend.academy.linktracker.bot.handler.statehandler;

import backend.academy.linktracker.bot.handler.StateChanger;
import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.state.AwaitTegState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AwaitLinkHandler extends StateChanger {

    @Autowired
    public AwaitLinkHandler(@Lazy AwaitTegState awaitTegState) {
        super(awaitTegState);
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        session.setLink(message.text());
        changeState(session);
        return "Введите через запятую теги для ссылки";
    }
}
