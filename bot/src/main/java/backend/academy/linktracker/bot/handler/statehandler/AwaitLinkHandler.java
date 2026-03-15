package backend.academy.linktracker.bot.handler.statehandler;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.state.AwaitTegState;
import backend.academy.linktracker.bot.model.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AwaitLinkHandler implements Handler {
    private final AwaitTegState awaitTegState;

    @Autowired
    public AwaitLinkHandler(AwaitTegState awaitTegState) {
        this.awaitTegState = awaitTegState;
    }

    //TODO сделать передачу session в аргументы метода, убрать session storage из данного уровня
    //TODO сделать валидацию ссылок
    @Override
    public String handle(UserMessage message,SessionData session) {
        session.setLink(message.text());
        session.setState(awaitTegState);
        return "Введите через запятую теги для ссылки";
    }
}

