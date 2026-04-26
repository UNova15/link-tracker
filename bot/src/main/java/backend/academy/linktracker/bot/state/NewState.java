package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.handler.UnknownUserHandler;
import backend.academy.linktracker.bot.handler.command.StartHandler;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NewState implements State {
    private final StartHandler handler;
    private final UnknownUserHandler unknownUserHandler;

    @Override
    public String process(UserMessage message, SessionData session) {

        // если команда не /start - неизвестная команда
        if (!message.text().equals(handler.getName())) {
            return unknownUserHandler.handle(message, session);
        }

        return handler.handle(message, session);
    }
}
