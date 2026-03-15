package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import org.springframework.stereotype.Component;

@Component
public class UnknownUserHandler implements Handler {

    @Override
    public String handle(UserMessage message, SessionData session) {
        return "Неизвестная команда. Чтобы начать диалог выполните команду /start";
    }

}
