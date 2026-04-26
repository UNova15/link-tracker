package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.domain.Command;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.state.AwaitLinkState;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class TrackHandler extends CommandHandler {
    private static final String MESSAGE = "Введите ссылку для отслеживания";

    public TrackHandler(@Lazy AwaitLinkState awaitLink) {
        super(new Command("/track", "Отслеживание ссылки"), awaitLink);
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        changeState(session);
        return MESSAGE;
    }
}
