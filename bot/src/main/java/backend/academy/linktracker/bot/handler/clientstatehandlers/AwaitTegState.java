package backend.academy.linktracker.bot.handler.clientstatehandlers;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.client.telegram.SessionStorage;
import backend.academy.linktracker.bot.handler.AddLinkHandler;
import backend.academy.linktracker.bot.model.BotState;
import backend.academy.linktracker.bot.util.TagParser;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AwaitTegState extends State {
    private final AddLinkHandler handler;
    private final SessionStorage storage;
    private final TagParser parser;

    public AwaitTegState(SessionStorage storage, AddLinkHandler handler, TagParser parser) {
        super(BotState.AWAIT_TAGS);
        this.storage = storage;
        this.handler = handler;
        this.parser = parser;
    }

    public SendMessage process(Update update){
        long chatId = update.message().chat().id();
        String message = update.message().text();

        List<String> tags = parser.parseTags(message);
        SessionData session = storage.findSession(chatId);

        session.setTags(tags);
        session.setState(BotState.AWAIT_COMMAND);

        return handler.process(chatId,session);
    }
}
