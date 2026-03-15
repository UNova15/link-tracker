package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.state.State;
import com.pengrad.telegrambot.model.BotCommand;


//TODO убрать BotCommand
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
