package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.model.UserMessage;
import org.springframework.stereotype.Component;

@Component
public class UnknownCommandHandler implements Handler {

    @Override
    public String handle(UserMessage message, SessionData session) {
        return "Неизвестная команда. Воспользуйтесь /help, чтобы посмотреть список доступных команд.";
    }
}
