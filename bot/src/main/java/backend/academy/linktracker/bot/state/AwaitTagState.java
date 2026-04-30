package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.handler.statehandler.AddLinkHandler;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AwaitTagState implements State {
    private final AddLinkHandler addLinkHandler;
    private final CommandRegistry commandRegistry;

    @Override
    public String process(UserMessage message, SessionData session) {
        return commandRegistry
                .getCommandHandler(message.text())
                .orElse(addLinkHandler)
                .handle(message, session);
    }
}
