package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.handler.statehandler.AwaitLinkHandler;
import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import org.springframework.stereotype.Component;

@Component
public class AwaitLinkState implements State {
    private final CommandRegistry commandRegistry;
    private final AwaitLinkHandler awaitLinkHandler;

    public AwaitLinkState(CommandRegistry commandRegistry, AwaitLinkHandler awaitLinkHandler) {
        this.awaitLinkHandler = awaitLinkHandler;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public String process(UserMessage message, SessionData session) {
        return commandRegistry
                .getCommandHandler(message.text())
                .orElse(awaitLinkHandler)
                .handle(message, session);
    }
}
