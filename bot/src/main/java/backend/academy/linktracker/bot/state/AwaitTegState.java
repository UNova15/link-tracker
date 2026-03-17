package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.handler.statehandler.AddLinkHandler;
import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import org.springframework.stereotype.Component;

@Component
public class AwaitTegState implements State {
    private final AddLinkHandler addLinkHandler;
    private final CommandRegistry commandRegistry;

    public AwaitTegState(CommandRegistry commandRegistry, AddLinkHandler addLinkHandler) {
        this.addLinkHandler = addLinkHandler;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public String process(UserMessage message, SessionData session) {
        return commandRegistry
                .getCommandHandler(message.text())
                .orElse(addLinkHandler)
                .handle(message, session);
    }
}
