package backend.academy.linktracker.bot.handler.clientstatehandlers;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.handler.command.AwaitLinkHandler;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AwaitLinkState implements State {
    private final CommandRegistry commandRegistry;
    private final AwaitLinkHandler awaitLinkHandler;

    public AwaitLinkState(CommandRegistry commandRegistry, AwaitLinkHandler awaitLinkHandler) {
        this.awaitLinkHandler= awaitLinkHandler;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public SendMessage process(Update update) {
        String message = update.message().text();

        Optional<Handler> handler = commandRegistry.getCommandHandler(message);

        //Не команда
        if(handler.isEmpty()){
            return awaitLinkHandler.handle(update);
        }

        //TODO по умолчанию изменение состояния будет определятся в классе CommandHandler в методе changeState
        // в котором будет определятся для конкретного обработчика изменяет он состояние или сбрасывает
        return handler.get().handle(update);
    }
}
