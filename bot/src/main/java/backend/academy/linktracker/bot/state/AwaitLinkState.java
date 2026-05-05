package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.statehandler.AwaitLinkHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AwaitLinkState implements State {
    private final CommandRegistry commandRegistry;
    private final AwaitLinkHandler awaitLinkHandler;

    @Override
    public String process(UserMessage message, SessionData session) {
        return commandRegistry
                .getCommandHandler(message.text())
                .orElse(awaitLinkHandler)
                .handle(message, session);
    }
}
