package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.model.Command;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class HelpHandler extends CommandHandler {
    private final CommandRegistry commands;

    @Autowired
    public HelpHandler(@Lazy CommandRegistry commands,@Lazy AwaitCommandState awaitCommandState) {
        super(new Command("/help", "Вывод списка всех доступных команд"),awaitCommandState);
        this.commands = commands;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        String commandsName = commands.toString();
        changeState(session);
        return "Список доступных команд:\n" + commandsName;
    }
}
