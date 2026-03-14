package backend.academy.linktracker.bot.client.clientstatehandlers;

import backend.academy.linktracker.bot.command.CommandHandler;
import backend.academy.linktracker.bot.command.StartHandler;
import backend.academy.linktracker.bot.command.UnknownUserHandler;
import backend.academy.linktracker.bot.model.BotState;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewStateHandler extends StateHandler {
    private final CommandHandler handler;
    private final UnknownUserHandler unknownUserHandler;

    @Autowired
    public NewStateHandler(StartHandler handler, UnknownUserHandler unknownUserHandler) {
        super(BotState.NEW);
        this.handler = handler;
        this.unknownUserHandler = unknownUserHandler;
    }

    @Override
    public SendMessage process(Update update) {
        String text = update.message().text();

        if (!text.equals(handler.getName())) {
            return unknownUserHandler.handle(update);
        }

        return handler.handle(update);
    }
}
