package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.model.Command;
import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CancelHandler extends CommandHandler {

    @Autowired
    public CancelHandler(@Lazy AwaitCommandState awaitCommandState) {
        super(new Command("/cancel", "Прекращение выполнения команды"), awaitCommandState);
    }

    public String handle(UserMessage message, SessionData session) {
        changeState(session);
        return "Операция отменена";
    }
}
