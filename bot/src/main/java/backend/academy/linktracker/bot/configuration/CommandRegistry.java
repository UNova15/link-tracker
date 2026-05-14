package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.handler.UnknownCommandHandler;
import backend.academy.linktracker.bot.handler.command.CommandHandler;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandRegistry {
    private final Map<String, CommandHandler> handlers;
    private final UnknownCommandHandler unknownCommandHandler;

    public Optional<Handler> getCommandHandler(String name) {
        return Optional.ofNullable(handlers.get(name));
    }

    public Handler getUnknownCommandHandler() {
        return unknownCommandHandler;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String command : handlers.keySet()) {
            builder.append(command);
            builder.append("\n");
        }
        return builder.toString();
    }
}
