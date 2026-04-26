package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.domain.Command;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CancelHandler extends CommandHandler {
    private static final String CANCEL_MESSAGE = "Операция отменена";

    public CancelHandler(@Lazy AwaitCommandState awaitCommandState) {
        super(new Command("/cancel", "Прекращение выполнения команды"), awaitCommandState);
    }

    public String handle(UserMessage message, SessionData session) {
        changeState(session);
        return CANCEL_MESSAGE;
    }
}
