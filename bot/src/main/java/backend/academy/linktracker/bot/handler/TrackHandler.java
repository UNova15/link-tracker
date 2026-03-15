package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.client.telegram.SessionStorage;
import backend.academy.linktracker.bot.handler.clientstatehandlers.AwaitLinkState;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackHandler extends CommandHandler {
    private final SessionStorage storage;
    private final AwaitLinkState awaitLink;

    @Autowired
    public TrackHandler(AwaitLinkState awaitLink,SessionStorage storage ) {
        super(new BotCommand("/track", "Отслеживание ссылки"));
        this.awaitLink = awaitLink;
        this.storage = storage;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();

        storage.updateSession(chatId,awaitLink);
        return new SendMessage(chatId,"Введите ссылку для отслеживания");
    }
}
