package backend.academy.linktracker.bot.command;

import com.pengrad.telegrambot.model.BotCommand;

public abstract class CommandHandler implements Handler {
    private final BotCommand command;

    public CommandHandler(BotCommand command) {
        this.command = command;
    }

    public String getName() {
        return command.command();
    }

    public String getDescription() {
        return command.description();
    }
}
