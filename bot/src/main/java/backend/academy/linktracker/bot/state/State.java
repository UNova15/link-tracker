package backend.academy.linktracker.bot.state;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;

public interface State {
    String process(UserMessage message, SessionData session);
}
