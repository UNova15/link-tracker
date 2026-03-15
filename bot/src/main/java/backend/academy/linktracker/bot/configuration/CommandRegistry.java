package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.handler.command.CommandHandler;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.handler.command.UnknownCommandHandler;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import backend.academy.linktracker.bot.handler.statehandler.UnknownUserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandRegistry {
    private final Map<String, CommandHandler> handlers;
    private final UnknownCommandHandler unknownCommandHandler;
    private final UnknownUserHandler unknownUserHandler;

    @Autowired
    public CommandRegistry(List<CommandHandler> handlers, UnknownCommandHandler unknownCommandHandler, UnknownUserHandler unknownUserHandler) {
        this.unknownCommandHandler = unknownCommandHandler;
        this.unknownUserHandler = unknownUserHandler;
        this.handlers = handlers.stream()
            .collect(Collectors.toMap(CommandHandler::getName, Function.identity()));
    }

    public Optional<Handler> getCommandHandler(String name) {
        return Optional.ofNullable(handlers.get(name));
    }

    public Handler getUnknownCommandHandler() {
        return unknownCommandHandler;
    }

    public Handler getUnknownUserHandler(){
        return unknownUserHandler;
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
