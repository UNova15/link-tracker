package backend.academy.linktracker.bot.handler.clientstatehandlers;

import backend.academy.linktracker.bot.handler.CommandHandler;
import backend.academy.linktracker.bot.handler.StartHandler;
import backend.academy.linktracker.bot.handler.UnknownUserHandler;
import backend.academy.linktracker.bot.model.BotState;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewState extends State {
    private final CommandHandler handler;
    private final UnknownUserHandler unknownUserHandler;

    @Autowired
    public NewState(StartHandler handler, UnknownUserHandler unknownUserHandler) {
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
