package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.client.telegram.SessionStorage;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.handler.clientstatehandlers.AwaitTegState;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AwaitLinkHandler implements Handler {
    private final AwaitTegState awaitTegState;
    private final SessionStorage storage;

    @Autowired
    public AwaitLinkHandler(SessionStorage storage, AwaitTegState awaitTegState) {
        this.storage = storage;
        this.awaitTegState = awaitTegState;
    }

    //TODO сделать передачу session в аргументы метода, убрать session storage из данного уровня
    //TODO сделать валидацию ссылок
    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        String message = update.message().text();

        SessionData session = storage.findSession(chatId);
        session.setLink(message);
        session.setState(awaitTegState);
        return new SendMessage(chatId, "Введите через запятую теги для ссылки");
    }
}

