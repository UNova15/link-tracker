package backend.academy.linktracker.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public abstract class CommandService {
    private final String commandName;

    public CommandService(String commandName) {
        this.commandName = commandName;
    }

    public abstract SendMessage handle(Update update);

    public String getCommandName() {
        return commandName;
    }
}
