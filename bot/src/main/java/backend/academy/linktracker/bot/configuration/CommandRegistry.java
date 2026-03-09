package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.service.telegramclient.AbstractHandler;
import backend.academy.linktracker.bot.service.telegramclient.Handler;
import backend.academy.linktracker.bot.service.telegramclient.UnknownCommandHandler;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandRegistry {
    private final Map<String, AbstractHandler> handlers;
    private final UnknownCommandHandler unknownCommandHandler;

    @Autowired
    public CommandRegistry(List<AbstractHandler> handlers, UnknownCommandHandler unknownCommandHandler) {
        this.unknownCommandHandler = unknownCommandHandler;
        this.handlers = handlers.stream()
            .collect(Collectors.toMap(AbstractHandler::getName, Function.identity()));
    }

    public Optional<Handler> getCommandHandler(String name) {
        return Optional.ofNullable(handlers.get(name));
    }

    public Handler getUnknownCommandHandler() {
        return unknownCommandHandler;
    }

    public Collection<AbstractHandler> getCommandHandlers() {
        return handlers.values();
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
