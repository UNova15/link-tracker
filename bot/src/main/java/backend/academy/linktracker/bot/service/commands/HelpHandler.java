package backend.academy.linktracker.bot.service.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HelpHandler extends CommandHandler {
    private List<CommandHandler> services;
    private String availableCommands;

    @Autowired
    public HelpHandler(List<CommandHandler> services) {
        super(new BotCommand("/help","Вывод списка всех доступных команд)"));
        this.services = services;
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        return new SendMessage(id, "Список доступных команд:\n" + availableCommands);
    }

    @PostConstruct
    public void init() {
        StringBuilder builder = new StringBuilder();

        for (CommandHandler command : services) {
            builder.append(command.getName());
            builder.append("\n");
        }
        this.availableCommands = builder.toString();
    }
}
