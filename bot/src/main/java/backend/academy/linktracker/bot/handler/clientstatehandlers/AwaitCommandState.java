package backend.academy.linktracker.bot.handler.clientstatehandlers;

import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.model.BotState;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class AwaitCommandState extends State {
    private final CommandRegistry registry;

    public AwaitCommandState(CommandRegistry registry) {
        super(BotState.AWAIT_COMMAND);
        this.registry = registry;
    }

    @Override
    public SendMessage process(Update update) {
        String text = update.message().text();
        Handler handler = registry.getCommandHandler(text).orElse(registry.getUnknownCommandHandler());

        return handler.handle(update);
    }
}
