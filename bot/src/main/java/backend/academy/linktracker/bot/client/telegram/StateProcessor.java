package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.handler.StartHandler;
import backend.academy.linktracker.bot.handler.clientstatehandlers.NewState;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateProcessor {
    private final StartHandler startHandler;
    private final CommandRegistry registry;
    private final SessionStorage storage;
    private final NewState newState;

    @Autowired
    public StateProcessor(SessionStorage storage, CommandRegistry registry, StartHandler startHandler, NewState newState) {
        this.storage = storage;
        this.registry = registry;
        this.startHandler = startHandler;
        this.newState = newState;
    }

    public SendMessage process(Update update) {
        long chatId = update.message().chat().id();
        String message = update.message().text();

        //TODO переделать с использованием Optional
        SessionData session = storage.findSession(chatId);
        if(session == null){
            session = storage.createSession(chatId,newState);
        }

        return session.getState().process(update);


        /*Optional<Handler> handler = registry.getCommandHandler(message);

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
        }*/


        /*SessionData session = storage.findSession(chatId);
        StateHandler handler = stateDispatcher.getStateHandler(session);
        return handler.process(update);*/
    }

}
