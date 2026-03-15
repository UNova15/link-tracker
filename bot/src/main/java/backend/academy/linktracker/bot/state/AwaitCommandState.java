package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.model.UserMessage;
import org.springframework.stereotype.Component;

@Component
public class AwaitCommandState implements State {
    private final CommandRegistry registry;

    public AwaitCommandState(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String process(UserMessage message, SessionData session) {
        Handler handler = registry.getCommandHandler(message.text())
            .orElse(registry.getUnknownCommandHandler());

        return handler.handle(message,session);
    }
}
