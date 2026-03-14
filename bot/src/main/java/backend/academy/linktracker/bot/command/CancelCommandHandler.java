package backend.academy.linktracker.bot.command;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.client.telegram.SessionStorage;
import backend.academy.linktracker.bot.model.BotState;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelCommandHandler extends CommandHandler {
    private final SessionStorage storage;

    @Autowired
    public CancelCommandHandler(SessionStorage storage) {
        super(new BotCommand("/cancel", "Прекращение выполнения команды"));
        this.storage = storage;
    }

    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        SessionData session = storage.findSession(id);

        session.setState(BotState.AWAIT_COMMAND);
        return new SendMessage(id, "Операция отменена");
    }
}
