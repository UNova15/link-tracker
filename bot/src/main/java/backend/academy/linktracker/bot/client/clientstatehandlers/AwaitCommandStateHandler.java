package backend.academy.linktracker.bot.client.clientstatehandlers;

import backend.academy.linktracker.bot.command.Handler;
import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.model.BotState;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class AwaitCommandStateHandler extends StateHandler {
    private final CommandRegistry registry;

    public AwaitCommandStateHandler(CommandRegistry registry) {
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
