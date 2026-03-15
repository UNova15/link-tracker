package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.handler.statehandler.AwaitLinkHandler;
import backend.academy.linktracker.bot.model.UserMessage;
import org.springframework.stereotype.Component;
import java.util.Optional;

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

        Optional<Handler> handler = commandRegistry.getCommandHandler(message.text());

        //Не команда
        if (handler.isEmpty()) {
            return awaitLinkHandler.handle(message,session);
        }
        return handler.get().handle(message,session);
    }
}
