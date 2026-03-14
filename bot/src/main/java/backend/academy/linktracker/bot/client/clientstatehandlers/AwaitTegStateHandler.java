package backend.academy.linktracker.bot.client.clientstatehandlers;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.client.telegram.SessionStorage;
import backend.academy.linktracker.bot.command.AddLinkHandler;
import backend.academy.linktracker.bot.model.BotState;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AwaitTegStateHandler extends StateHandler{
    private final AddLinkHandler handler;
    private final SessionStorage storage;

    public AwaitTegStateHandler(SessionStorage storage, AddLinkHandler handler) {
        super(BotState.AWAIT_LINK);
        this.storage = storage;
        this.handler = handler;
    }

    //TODO добавить парсинг тегов
    public SendMessage process(Update update){
        long chatId = update.message().chat().id();
        String tags = update.message().text();

        SessionData session = storage.findSession(chatId);

        session.setTags(List.of(tags));
        session.setState(BotState.AWAIT_COMMAND);

        return handler.process(chatId,session);
    }
}
