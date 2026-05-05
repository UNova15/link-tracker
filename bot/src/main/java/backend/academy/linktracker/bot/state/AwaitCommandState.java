package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AwaitCommandState implements State {
    private final CommandRegistry registry;
    private final RequestArgsParser parser;

    @Override
    public String process(UserMessage message, SessionData session) {
        String command = parser.parseCommand(message.text());

        Handler handler = registry.getCommandHandler(command).orElseGet(registry::getUnknownCommandHandler);

        return handler.handle(message, session);
    }
}
