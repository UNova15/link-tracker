package backend.academy.linktracker.bot.service.telegramclient;

import com.pengrad.telegrambot.model.BotCommand;

public abstract class AbstractHandler implements Handler {
    private final BotCommand command;

    public AbstractHandler(BotCommand command) {
        this.command = command;
    }

    public String getName() {
        return command.command();
    }

    public String getDescription() {
        return command.description();
    }
}
