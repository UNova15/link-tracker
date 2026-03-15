package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.handler.statehandler.AddLinkHandler;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.model.UserMessage;
import org.springframework.stereotype.Component;
import java.util.Optional;

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

        Optional<Handler> handler = commandRegistry.getCommandHandler(message.text());
        //Не команда
        if (handler.isEmpty()) {
            return addLinkHandler.handle(message,session);
        }
        return handler.get().handle(message,session);
    }
}
