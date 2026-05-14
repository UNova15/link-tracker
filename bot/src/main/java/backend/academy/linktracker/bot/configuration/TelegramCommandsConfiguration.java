package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.handler.UnknownCommandHandler;
import backend.academy.linktracker.bot.handler.command.CommandHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramCommandsConfiguration {

    @Bean
    public InitializingBean registerBotCommands(TelegramBot telegramBot, List<CommandHandler> handlers) {
        return () -> {
            List<BotCommand> commands = handlers.stream()
                    .map(handler -> new BotCommand(handler.getName(), handler.getDescription()))
                    .toList();

            telegramBot.execute(new SetMyCommands(commands.toArray(new BotCommand[0])));
        };
    }

    @Bean
    public CommandRegistry commandRegistry(List<CommandHandler> handlers, UnknownCommandHandler unknownCommandHandler) {
        Map<String, CommandHandler> handlersByName =
                handlers.stream().collect(Collectors.toMap(CommandHandler::getName, Function.identity()));
        return new CommandRegistry(handlersByName, unknownCommandHandler);
    }
}
