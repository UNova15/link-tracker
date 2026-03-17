package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import org.springframework.stereotype.Component;

@Component
public class AwaitCommandState implements State {
    private final CommandRegistry registry;
    private final RequestArgsParser parser;

    public AwaitCommandState(CommandRegistry registry, RequestArgsParser parser) {
        this.registry = registry;
        this.parser = parser;
    }

    @Override
    public String process(UserMessage message, SessionData session) {
        String command = parser.parseCommand(message.text());

        Handler handler = registry.getCommandHandler(command).orElse(registry.getUnknownCommandHandler());

        return handler.handle(message, session);
    }
}
