package backend.academy.linktracker.bot.handler.clientstatehandlers;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.client.telegram.SessionStorage;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.model.BotState;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AwaitLinkState extends State {
    private final SessionStorage storage;
    private final CommandRegistry commandRegistry;

    public AwaitLinkState(SessionStorage storage, CommandRegistry commandRegistry) {
        super(BotState.AWAIT_LINK);
        this.storage = storage;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public SendMessage process(Update update) {
        long chatId = update.message().chat().id();
        String message = update.message().text();

        Optional<Handler> handler = commandRegistry.getCommandHandler(message);

        if(handler.isEmpty()){

            SessionData session = storage.findSession(chatId);
            session.setLink(message);
            session.setState(BotState.AWAIT_TAGS);

            return new SendMessage(chatId,"Введите через запятую теги для ссылки");
        }

        return handler.get().handle(update);
    }
}
