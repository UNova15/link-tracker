package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.state.AwaitLinkState;
import backend.academy.linktracker.bot.model.UserMessage;
import com.pengrad.telegrambot.model.BotCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackHandler extends CommandHandler {
    private final AwaitLinkState awaitLink;

    @Autowired
    public TrackHandler(AwaitLinkState awaitLink) {
        super(new BotCommand("/track", "Отслеживание ссылки"));
        this.awaitLink = awaitLink;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        session.setState(awaitLink);
        return "Введите ссылку для отслеживания";
    }
}
