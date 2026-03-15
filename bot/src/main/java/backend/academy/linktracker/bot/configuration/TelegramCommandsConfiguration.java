package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.handler.CommandHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import java.util.List;
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
}
