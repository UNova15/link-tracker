package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.handler.StartHandler;
import backend.academy.linktracker.bot.model.BotState;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StateProcessor {
    private final StartHandler startHandler;
    private final CommandRegistry registry;
    private final SessionStorage storage;
    private final StateDispatcher stateDispatcher;

    @Autowired
    public StateProcessor(StateDispatcher stateDispatcher, SessionStorage storage, CommandRegistry registry, StartHandler startHandler) {
        this.stateDispatcher = stateDispatcher;
        this.storage = storage;
        this.registry = registry;
        this.startHandler = startHandler;
    }

    public SendMessage process(Update update) {
        long chatId = update.message().chat().id();
        String message = update.message().text();

        Optional<Handler> handler = registry.getCommandHandler(message);

        //Обработка сообщения как команды
        SessionData session = storage.findSession(chatId);

        if (handler.isPresent()) {
            if (session.getState() == BotState.NEW && startHandler.getName().equals(message))
                return startHandler.handle(update);

            if(session.getState()!=BotState.NEW){
                return handler.get().handle(update);
            }

            return registry.getUnknownUserHandler().handle(update);

            //обработка сообщения не как команды
        } else {
            return stateDispatcher.getStateHandler(session).process(update);
        }


        /*SessionData session = storage.findSession(chatId);
        StateHandler handler = stateDispatcher.getStateHandler(session);
        return handler.process(update);*/
    }

}
