package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.service.commands.CommandHandler;
import backend.academy.linktracker.bot.service.commands.Handler;
import backend.academy.linktracker.bot.service.commands.UnknownCommandHandler;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandRegistry {
    private final Map<String, CommandHandler> handlers;
    private final UnknownCommandHandler unknownCommandHandler;

    @Autowired
    public CommandRegistry(List<CommandHandler> handlers, UnknownCommandHandler unknownCommandHandler) {
        this.unknownCommandHandler = unknownCommandHandler;
        this.handlers = handlers.stream().collect(Collectors.toMap(CommandHandler::getName, handler -> handler));
    }

    public Optional<Handler> getCommandHandler(String name) {
        return Optional.ofNullable(handlers.get(name));
    }

    public Handler getUnknownCommandHandler() {
        return unknownCommandHandler;
    }

    public Collection<CommandHandler> getCommandHandlers() {
        return handlers.values();
    }
}
