package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import com.pengrad.telegrambot.model.BotCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class HelpHandler extends CommandHandler {
    private final CommandRegistry commands;
    private final AwaitCommandState awaitCommandState;

    @Autowired
    public HelpHandler(@Lazy CommandRegistry commands) {
        super(new BotCommand("/help", "Вывод списка всех доступных команд"));
        this.commands = commands;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        String commandsName = commands.toString();
        session.setState();
        return "Список доступных команд:\n" + commandsName;
    }
}
