package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.model.Command;
import backend.academy.linktracker.bot.state.AwaitLinkState;
import backend.academy.linktracker.bot.model.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class TrackHandler extends CommandHandler {

    @Autowired
    public TrackHandler(@Lazy AwaitLinkState awaitLink) {
        super(new Command("/track", "Отслеживание ссылки"),awaitLink);
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        changeState(session);
        return "Введите ссылку для отслеживания";
    }
}
