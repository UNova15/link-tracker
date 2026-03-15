package backend.academy.linktracker.bot.handler.clientstatehandlers;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.handler.AddLinkHandler;
import backend.academy.linktracker.bot.handler.Handler;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AwaitTegState implements State {
    private final AddLinkHandler addLinkHandler;
    private final CommandRegistry commandRegistry;

    public AwaitTegState(CommandRegistry commandRegistry, AddLinkHandler addLinkHandler) {
        this.addLinkHandler = addLinkHandler;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public SendMessage process(Update update){
        long chatId = update.message().chat().id();
        String message = update.message().text();


        Optional<Handler> handler = commandRegistry.getCommandHandler(message);
        //Не команда
        if(handler.isEmpty()){
            return addLinkHandler.process(update);
        }

        //TODO по умолчанию изменение состояния будет определятся в классе CommandHandler в методе changeState
        // в котором будет определятся для конкретного обработчика изменяет он состояние или сбрасывает
        return handler.get().handle(update);
    }
}
