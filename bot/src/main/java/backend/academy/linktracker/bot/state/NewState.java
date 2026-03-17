package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.handler.UnknownUserHandler;
import backend.academy.linktracker.bot.handler.command.CommandHandler;
import backend.academy.linktracker.bot.handler.command.StartHandler;
import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewState implements State {
    private final CommandHandler handler;
    private final UnknownUserHandler unknownUserHandler;

    @Autowired
    public NewState(StartHandler handler, UnknownUserHandler unknownUserHandler) {
        this.handler = handler;
        this.unknownUserHandler = unknownUserHandler;
    }

    @Override
    public String process(UserMessage message, SessionData session) {

        // если команда не /start - неизвестная команда
        if (!message.text().equals(handler.getName())) {
            return unknownUserHandler.handle(message, session);
        }

        return handler.handle(message, session);
    }
}
