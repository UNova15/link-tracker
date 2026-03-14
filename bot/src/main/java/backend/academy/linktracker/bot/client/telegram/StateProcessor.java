package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.client.clientstatehandlers.StateHandler;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateProcessor {
    private final SessionStorage storage;
    private final StateDispatcher stateDispatcher;

    @Autowired
    public StateProcessor(StateDispatcher stateDispatcher, SessionStorage storage) {
        this.stateDispatcher = stateDispatcher;
        this.storage = storage;
    }

    public SendMessage process(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        SessionData session = storage.findSession(chatId);
        StateHandler handler = stateDispatcher.getStateHandler(session);
        //TODO добавить session в аргументы .process
        return handler.process(update);
    }

}
