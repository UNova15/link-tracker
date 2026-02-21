package backend.academy.linktracker.bot.service.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public abstract class CommandHandler {
    private final BotCommand command;

    public CommandHandler(BotCommand command) {
        this.command = command;
    }

    public abstract SendMessage handle(Update update);

    public String getName(){
        return command.command();
    }

    public String getDescription(){
        return command.description();
    }
}
