package backend.academy.linktracker.bot.service.commands;

import backend.academy.linktracker.bot.CommandRegistry;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class HelpHandler extends CommandHandler {
    @Autowired
    @Lazy
    private CommandRegistry commands;

    public HelpHandler() {
        super(new BotCommand("/help", "Вывод списка всех доступных команд"));
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        String commands = commandsForming();
        return new SendMessage(id, "Список доступных команд:\n" + commands);
    }

    private String commandsForming() {
        StringBuilder builder = new StringBuilder();

        for (CommandHandler command : commands.getCommandHandlers()) {
            builder.append(command.getName());
            builder.append("\n");
        }
        return builder.toString();
    }
}
