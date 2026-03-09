package backend.academy.linktracker.bot.service.telegramclient;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class HelpHandler extends AbstractHandler {
    private final CommandRegistry commands;

    @Autowired
    public HelpHandler(@Lazy CommandRegistry commands) {
        super(new BotCommand("/help", "Вывод списка всех доступных команд"));
        this.commands = commands;
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        String commandsName = commands.toString();
        return new SendMessage(id, "Список доступных команд:\n" + commandsName);
    }
}
