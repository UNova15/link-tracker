package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.client.telegram.SessionStorage;
import backend.academy.linktracker.bot.handler.clientstatehandlers.AwaitCommandState;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelHandler extends CommandHandler {
    private final SessionStorage storage;
    private final AwaitCommandState awaitCommandState;

    @Autowired
    public CancelHandler(SessionStorage storage,AwaitCommandState awaitCommandState) {
        super(new BotCommand("/cancel", "Прекращение выполнения команды"));
        this.storage = storage;
        this.awaitCommandState = awaitCommandState;
    }

    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        SessionData session = storage.findSession(id);
        session.setState(awaitCommandState);

        return new SendMessage(id, "Операция отменена");
    }
}
