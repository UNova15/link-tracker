package backend.academy.linktracker.bot.handler.clientstatehandlers;

import backend.academy.linktracker.bot.handler.CommandHandler;
import backend.academy.linktracker.bot.handler.StartHandler;
import backend.academy.linktracker.bot.handler.UnknownUserHandler;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewState implements State {
    private final CommandHandler handler;
    private final UnknownUserHandler unknownUserHandler;

    @Autowired
    public NewState(StartHandler handler, UnknownUserHandler unknownUserHandler) {
        this.handler = handler;
        this.unknownUserHandler = unknownUserHandler;
    }

    @Override
    public SendMessage process(Update update) {
        String text = update.message().text();

        //если команда не /start - неизвестная команда
        if (!text.equals(handler.getName())) {
            return unknownUserHandler.handle(update);
        }

        return handler.handle(update);
    }
}
