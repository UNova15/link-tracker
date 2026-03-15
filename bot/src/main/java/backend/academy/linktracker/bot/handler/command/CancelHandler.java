package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.model.UserMessage;
import com.pengrad.telegrambot.model.BotCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelHandler extends CommandHandler {
    private final AwaitCommandState awaitCommandState;

    @Autowired
    public CancelHandler(AwaitCommandState awaitCommandState) {
        super(new BotCommand("/cancel", "Прекращение выполнения команды"));
        this.awaitCommandState = awaitCommandState;
    }

    public String handle(UserMessage message,SessionData session) {

        session.setState(awaitCommandState);
        return "Операция отменена";
    }
}
